var StackBusterUtil = {};

/**
 * Checks if question link/badge combination is valid
 * Makes API request to check question id and user id
 *
 * @param questionLink question link
 * @param badge badge type
 * @param user logged in user
 * @param callback Node-style callback(err, data)
 */
StackBusterUtil.check = function (questionLink, badge, user, callback) {

    if (!StackBusterAPI.BadgeType.isValid(badge)) {
        callback(new Error("Invalid badge type: " + badge));
    }

    if (questionLink && typeof questionLink === 'string') {
        var m, qid, uid;
        var qRegex = /questions\/(\d+)/,
            qLinkRegex = /q\/(\d+)\/(\d+)/,
            qLinkRegexShort = /q\/(\d+)/;

        if ((m = questionLink.match(qRegex)) && m.length === 2) {
            qid = m[1]
        } else if ((m = questionLink.match(qLinkRegex)) && m.length === 3) {
            qid = m[1];
            uid = m[2]
        } else if ((m = questionLink.match(qLinkRegexShort)) && m.length === 2) {
            qid = m[1]
        }

        if (qid) {
            axios.get(StackBusterAPI.Urls.QUESTION(qid))
                .then(function (response) {
                    var items = response.data.items;
                    if (items.length) {
                        if (items[0].question_id === parseInt(qid)) {
                            if (StackBusterAPI.BadgeType.isUserInvolved(badge)) {
                                if (uid) {
                                    axios.get(StackBusterAPI.Urls.USER(uid))
                                        .then(function (response) {
                                            var items = response.data.items;
                                            if (items.length) {
                                                if (items[0].user_id === parseInt(uid)) {
                                                    callback(null, {
                                                        qid: qid,
                                                        type: 'user',
                                                        question: {
                                                            uid: uid,
                                                            user_id: user.uid,
                                                            badgeType: badge
                                                        }
                                                    })
                                                } else {
                                                    // branches like these shouldn't really be reached
                                                    callback(new Error("Wrong question link: provided uid " + uid
                                                        + " doesn't match actual uid " + items[0].user_id));
                                                }
                                            } else {
                                                callback(new Error("User with id " + uid + " does not exist"));
                                            }
                                        })
                                        .catch(function (error) {
                                            callback(new Error("Uid in question link cannot be verified: "
                                                + error));
                                        })
                                } else {
                                    callback(new Error("Invalid user id: " + uid));
                                }
                            } else {
                                callback(null, {
                                    qid: qid,
                                    type: 'default',
                                    question: {
                                        user_id: user.uid,
                                        badgeType: badge
                                    }
                                })
                            }
                        } else {
                            // branches like these shouldn't really be reached
                            callback(new Error("Wrong question link: provided qid " + qid
                                + " doesn't match actual qid " + items[0].question_id));
                        }
                    } else {
                        callback(new Error("Question with id " + qid + " does not exist"));
                    }
                })
                .catch(function (error) {
                    callback(new Error("Question link cannot be verified: " + error));
                })
        } else {
            callback(new Error("Invalid question id: " + qid));
        }
    } else {
        callback(new Error("Invalid/empty question link"));
    }
};
