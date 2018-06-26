var StackBusterAPI = {};

StackBusterAPI.BadgeType = Object.create(null);

StackBusterAPI.BadgeType.POPULAR_QUESTION = 'POPULAR_QUESTION';
StackBusterAPI.BadgeType.NOTABLE_QUESTION = 'NOTABLE_QUESTION';
StackBusterAPI.BadgeType.FAMOUS_QUESTION = 'FAMOUS_QUESTION';

StackBusterAPI.BadgeType.ANNOUNCER = 'ANNOUNCER';
StackBusterAPI.BadgeType.BOOSTER = 'BOOSTER';
StackBusterAPI.BadgeType.PUBLICIST = 'PUBLICIST';

StackBusterAPI.BadgeType.isUserInvolved = function (type) {
    return type === this.ANNOUNCER || type === this.BOOSTER || type === this.PUBLICIST
};
StackBusterAPI.BadgeType.isValid = function (type) {
    for (var key in this) {
        // avoided need for extra `this.hasOwnProperty(key)` check
        if (typeof this[key] !== 'function' && this[key] === type)
            return true
    }
};

StackBusterAPI.ProgressType = {
    COMPLETED: 'COMPLETED',
    IN_PROGRESS: 'IN_PROGRESS',
    ABORTED: 'ABORTED',
    DELETED: 'DELETED',
};

StackBusterAPI.Urls = {
    QUESTION: function (qid) {
        return 'https://api.stackexchange.com/2.2/questions/' + qid + '?order=desc&sort=activity&site=stackoverflow'
    },
    USER: function (uid) {
        return 'https://api.stackexchange.com/2.2/users/' + uid + '?order=asc&sort=reputation&site=stackoverflow'
    }
};

/**
 * Adds question item to database for batch processing
 *
 * @param questionObject valid question object
 * @param callback Node-style callback(err, data)
 */
StackBusterAPI.enqueue = function (questionObject, callback) {
    firebase.database()
        .ref('questions/' + questionObject.type + '/' + questionObject.qid)
        .set(StackBusterAPI._question_adapter(questionObject.question))
        .then(function () {
            callback(null, {
                name: 'Info',
                message: 'Question with id ' + questionObject.qid + ' has been added for processing.'
            })
        })
        .catch(function (error) {
            callback(error)
        })
    ;
};

/**
 * Removes question item from database
 *
 * @param question question
 */
StackBusterAPI.remove = function (question) {
    firebase.database()
        .ref('questions/' + question.type + '/' + question.id + '/progress')
        .set(StackBusterAPI.ProgressType.DELETED)
        .then(function () {
            callback(null, {
                name: 'Info',
                message: 'Question with id ' + questionObject.qid + ' has been removed for processing.'
            })
        })
        .catch(function (error) {
            callback(error)
        })
    ;
};

/**
 * Loads queued questions for current user
 *
 * @param callback with questions object
 */
StackBusterAPI.loadQuestions = function (callback) {
    firebase.database()
        .ref('questions/user')
        .orderByChild("user_id")
        .equalTo(window.stackBusterUser.uid)
        .on('value', function (snapshot) {
            var adapted = StackBusterAPI._questions_object_adapter(snapshot.val());
            callback(adapted, 'user')
        })
    ;
    firebase.database()
        .ref('questions/default')
        .orderByChild("user_id")
        .equalTo(window.stackBusterUser.uid)
        .on('value', function (snapshot) {
            var adapted = StackBusterAPI._questions_object_adapter(snapshot.val());
            callback(adapted, 'default')
        })
};

StackBusterAPI._question_adapter = function (question) {
    var uid = question.uid;
    return {
        t: {
            badgeType: question.badgeType,
            user_id: question.user_id
        },
        uid: uid
    }
};

StackBusterAPI._questions_object_adapter = function (questionsObject) {
    var adapted = Object.create(null);
    for (var key in questionsObject) {
        if (questionsObject.hasOwnProperty(key)) {
            adapted[key] = Object.assign({}, questionsObject[key].t, {uid: questionsObject[key].uid});
        }
    }
    return adapted
}
