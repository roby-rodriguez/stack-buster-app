var StackBusterUI = (new function () {
    this.show = function (id) {
        document.getElementById(id).style.display = 'block';
    };

    this.hide = function (id) {
        document.getElementById(id).style.display = 'none';
    };

    /**
     * Display welcome logo
     *
     * @param id container id
     * @param user logged in user
     */
    this.buildLogo = function (id, user) {
        var header = document.getElementById(id),
            avatar = document.createElement('img'),
            welcome = document.createElement('H3'),
            welcomeText = document.createTextNode('Welcome, ' + user.displayName + '!');

        avatar.src = user.photoURL;
        avatar.classList.add('sb-avatar');
        header.appendChild(avatar);
        welcome.appendChild(welcomeText);
        header.appendChild(welcome);
    };

    /**
     * Display messages
     *
     * @param message info or error message
     */
    this.buildMessage = function (message) {
        var container = document.getElementById('sb-message-container'),
            close = document.createElement('span'),
            title = document.createElement('strong'),
            self = this;

        // initialize stuff if necessary
        message = message || {};
        message.name = message.name || 'Error';
        message.message = message.message || 'Something went wrong.';

        container.className = 'alert alert-' + message.name;
        container.innerHTML = null;
        close.addEventListener('click', function () {
            self.hide('sb-message-container')
        });
        close.className = 'close-btn';
        close.appendChild(document.createTextNode('\u02DF'));
        container.appendChild(close);
        title.appendChild(document.createTextNode(message.name));
        container.appendChild(title);
        container.appendChild(document.createTextNode(message.message));
        this.show('sb-message-container')
    };

    /**
     * Displays questions to table
     *
     * @param id table id
     * @param questionObject question object
     * @param type (optional) 'user' or 'default' question type
     */
    this.buildQuestions = function (id, questionObject, type) {
        var table = document.getElementById(id).getElementsByTagName('tbody')[0];
        // always clear out table
        if (!type || type === 'user')
            table.innerHTML = null;

        for (var key in questionObject) {
            if (questionObject.hasOwnProperty(key)) {
                var question = Object.assign({}, questionObject[key], {id: key}, {type: type});
                _appendQuestion(table, question)
            }
        }
    };

    function _appendQuestion(table, question) {
        var tr = document.createElement('tr'),
            tdq = document.createElement('td'),
            tdc = document.createElement('td'),
            tdi = document.createElement('td');
        tdq.classList.add('sb-status-table-cell-question');
        tdq.appendChild(document.createTextNode(question.id));

        var thcc = document.createElement('div'),
            thccc = document.createElement('div'),
            thccp = document.createElement('div');

        thcc.classList.add('sb-status-table-cell-completed-progress-container');

        var progress = parseInt(question.completed);
        if (progress < 15) {
            var thccpt = document.createElement('span');
            thccc.style.width = '15%';
            thccpt.classList.add('sb-status-table-cell-completed-progress-text');
            thccpt.appendChild(document.createTextNode(question.completed));
            thccp.appendChild(thccpt)
        } else {
            thccc.style.width = question.completed;
            thccp.appendChild(document.createTextNode(question.completed));
        }
        thccp.classList.add('sb-status-table-cell-completed-progress');
        thccc.appendChild(thccp);
        thcc.appendChild(thccc);
        tdc.classList.add('sb-status-table-cell-completed');
        tdc.appendChild(thcc);

        var img = document.createElement('img');
        img.src = 'resources/trash.png';
        img.addEventListener('click', function () {
            StackBusterAPI.remove(question)
        });
        img.classList.add('sb-status-table-delete');
        tdi.appendChild(img);

        tr.appendChild(tdq);
        tr.appendChild(tdc);
        tr.appendChild(tdi);

        table.appendChild(tr);
    }
}());
