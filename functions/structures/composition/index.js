const functions = require('firebase-functions')
const admin = require('firebase-admin')
const ProgressType = require('./types/progress.type')

admin.initializeApp(functions.config().firebase)

exports.trackUser = functions.auth
    .user()
    .onCreate(result => {
        // { email, emailVerified, photoURL, uid }
        const { uid } = result.data,
            now = new Date().getTime()

        return admin.database()
            .ref(`/users/${uid}`)
            .set({
                created: now,
                lastActive: now,
                activeQuestions: 0,
                totalQuestions: 0,
            })
    })

exports.sanitizeDefault = functions.database
    .ref('/questions/default/{qid}')
    .onWrite(event => {
        // do nothing when delete (except user data cleanup - if necessary)
        if (!event.data.exists()) {
            const { t } = event.data.previous.val(),
                { completed, user_id } = t;

            if (!completed || parseFloat(completed) === 0) {
                const qid = event.params.qid
                // question did not manage to have any activity -> allow removal & track history
                // cleanup current user
                return updateUser(user_id, false, false)
                    // log history entry
                    .then(() => trackHistory(qid, 'default'))
            } else {
                // question has activity - question counts as total but allow removal & track history (aka no money back)
                return updateUser(user_id, false)
            }
        }

        const { t } = event.data.val(),
            { completed, user_id } = t,
            ref = event.data.ref

        // if object properties uninitialized
        if (!completed) {
            // then initialize
            return updateUser(user_id, true, true)
                .then(() => ref.child('t').update({
                    completed: '0%',
                }))
                .catch(error => ref.remove())

        }

        return null
    })

exports.sanitizeUser = functions.database
    .ref('/questions/user/{qid}')
    .onWrite(event => {
        // do nothing when delete (except user data cleanup - if necessary)
        if (!event.data.exists()) {
            const { t, uid } = event.data.previous.val(),
                { completed, user_id } = t;

            if (!completed || parseFloat(completed) === 0) {
                const qid = event.params.qid
                // cleanup stack user
                return updateStackUser(uid, false)
                    // cleanup current user
                    .then(() => updateUser(user_id, false, false))
                    // log history entry
                    .then(() => trackHistory(qid, 'user'))
            } else {
                // question has activity - question counts as total but allow removal & track history (aka no money back)
                return updateUser(user_id, false)
            }
        }

        const { t, uid } = event.data.val(),
            { completed, user_id } = t,
            ref = event.data.ref

        // if object properties uninitialized
        if (!completed) {
            // then initialize user and question object
            return updateStackUser(uid)
                .then(() => updateUser(user_id, true, true))
                .then(() => ref.child('t').update({
                    completed: '0%',
                }))
                .catch(error => ref.remove())
        }

        return null
    })

const updateStackUser = (uid, increment = true) => admin.database()
    .ref(`/stackUsers/${uid}`)
    .transaction(user => {
        if (user) {
            user.totalQuestions = increment ? user.totalQuestions + 1 : user.totalQuestions - 1
            return user
        }
        return {
            totalQuestions: 1
        }
    })

const updateUser = (user_id, incrementActive, incrementTotal) => admin.database()
    .ref(`/users/${user_id}`)
    .transaction(user => {
        if (user) {
            user.lastActive = new Date().getTime()
            user.activeQuestions = incrementActive ? user.activeQuestions + 1 : user.activeQuestions - 1
            if (typeof user.totalQuestions !== 'undefined')
                user.totalQuestions = incrementTotal ? user.totalQuestions + 1 : user.totalQuestions - 1
        }
        return user
    })

const trackHistory = (qid, type) => admin.database()
    .ref(`workingQuestions/${type}/${qid}`)
    .once('value', wq => {
        const wqRef = wq.ref,
            workingQuestion = wq.val()
        if (!workingQuestion) return null
        return admin.database()
            .ref(`history/${qid}`)
            .set(Object.assign({}, workingQuestion, {
                progress: ProgressType.DELETED,
                ended: new Date().getTime()
            }))
            .then(() => wqRef.remove())
    })
