const functions = require('firebase-functions')
const admin = require('firebase-admin')

admin.initializeApp(functions.config().firebase)

exports.sanitize = functions.database
    .ref('/questions/{qid}')
    .onWrite(event => {
        // do nothing when delete
        if (!event.data.exists()) {
            return null
        }

        const { completed } = event.data.val(),
            ref = event.data.ref

        // if object properties uninitialized
        if (!completed) {
            // then initialize
            return ref.update({
                completed: '0%',
            })
        }

        return null
    })
