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

        const { user_id, badgeType, uid } = event.data.val(),
            qid = event.params.qid,
            ref = event.data.ref

        // qid should contain only digits
        if (/^\d+$/.test(qid)) {
            // if data has no qid / badge type, do nothing
            if (!BadgeType.isValid(badgeType)) return ref.remove()
            // if user badge check uid
            if (BadgeType.isUserInvolved(badgeType) && !(uid && /^\d+$/.test(uid)))
                return ref.remove()
            // check if authentication exists
            if (user_id && user_id.length) {
                // if validated then initialize
                return ref.update({
                    completed: '0%',
                })
            }
        }

        return ref.remove()
    })
