const functions = require('firebase-functions')
const admin = require('firebase-admin')
const BadgeType = require('./types/badge.type')

admin.initializeApp(functions.config().firebase)

exports.sanitize = functions.database
    .ref('/questions/{qid}')
    .onWrite(event => {
        // do nothing when delete
        if (!event.data.exists()) {
            return null
        }

        const { completed, uid, badgeType } = event.data.val(),
            ref = event.data.ref

        // if object properties uninitialized
        if (!completed) {
            // if user badge check valid uid
            if (BadgeType.isUserInvolved(badgeType) && !(uid && /^\d+$/.test(uid)))
                return ref.remove()
            // then initialize
            return ref.update({
                completed: '0%',
            })
        }

        return null
    })
