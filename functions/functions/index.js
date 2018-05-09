const request = require('request')
const functions = require('firebase-functions')
const admin = require('firebase-admin')

admin.initializeApp(functions.config().firebase)

const StackApiUrls = {
    QUESTION: (qid) => `https://api.stackexchange.com/2.2/questions/${qid}?order=desc&sort=activity&site=stackoverflow`,
    USER: (uid) => `https://api.stackexchange.com/2.2/users/${uid}?order=asc&sort=reputation&site=stackoverflow`
}

const BadgeType = {
    POPULAR_QUESTION: 'POPULAR_QUESTION',
    NOTABLE_QUESTION: 'NOTABLE_QUESTION',
    FAMOUS_QUESTION: 'FAMOUS_QUESTION',

    ANNOUNCER: 'ANNOUNCER',
    BOOSTER: 'BOOSTER',
    PUBLICIST: 'PUBLICIST',

    isUserInvolved: function (type) {
        return type === this.ANNOUNCER || type === this.BOOSTER || type === this.PUBLICIST
    },
    isValid: function (type) {
        for (let key in this) {
            if (this[key] === type)
                return true
        }
    }
}

const initialize = (reqData, qData) => ({
    uid: reqData.uid,
    badge: reqData.badgeType,
    created: new Date().getTime(),
    views: qData.view_count,
    count: 0
})

// check if question exists
const updateRef = (ref, data, qid) => request(StackApiUrls.QUESTION(qid), { gzip: true },
    (error, response, body) => {
        if (error) return
        const question = JSON.parse(body)

        if (!question.items.length) return

        const intialized = initialize(data, question.items[0])
        return ref.set(intialized)
    })

exports.sanitize = functions.database
    .ref('/questions/{qid}')
    .onWrite(event => {
        const data = event.data.val(),
            qid = event.params.qid,
            parentRef = event.data.ref.parent

        // if data has no qid / badge type, do nothing
        if (!BadgeType.isValid(data.badgeType)) return

        if (BadgeType.isUserInvolved(data.badgeType)) {

            // if user involved but no uid, do nothing
            if (!data.uid) return

            // check out user if exists
            return request(StackApiUrls.USER(data.uid), { gzip: true }, (error, response, body) => {
                if (error) return
                const user = JSON.parse(body)

                if (!user.items.length) return

                return updateRef(event.data.ref, data, qid)
            })
        } else {
            request(StackApiUrls.QUESTION(qid), { gzip: true },
                (error, response, body) => {
                    console.log(error)
                    if (error) return
                    const question = JSON.parse(body)

                    if (!question.items.length) return

                    const intialized = initialize(data, question.items[0])
                    return ref.set(intialized)
                })
            return updateRef(event.data.ref, data, qid)
        }
    })
