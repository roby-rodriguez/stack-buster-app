var StackBusterUI = (new function () {
    this.show = function (id) {
        document.getElementById(id).style.display = 'block';
    };

    this.hide = function (id) {
        document.getElementById(id).style.display = 'none';
    };

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

    this.buildError = function (error) {
        // TODO
    };

    this.buildQuestions = function (id, questionObject) {
        var table = document.getElementById(id).getElementsByTagName('tbody')[0];
        // always clear out table
        table.innerHTML = null;

        for (var key in questionObject) {
            if (questionObject.hasOwnProperty(key)) {
                var question = Object.assign({}, questionObject[key], {id: key});
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
        if (progress < 10) {
            var thccpt = document.createElement('span');
            thccc.style.width = '7%';
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
        img.addEventListener('click', function (e) {
            // TODO bind firebase remove
            console.log('Removing qid', question.id)
        });
        img.classList.add('sb-status-table-delete');
        tdi.appendChild(img);

        tr.appendChild(tdq);
        tr.appendChild(tdc);
        tr.appendChild(tdi);

        table.appendChild(tr);
    }
}());
