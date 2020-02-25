<#macro page title>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>${title}</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <link rel="shortcut icon" href="/icon/favicon.ico" type="image/png">
        <link rel="stylesheet" href="/css/bootstrap.css">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css"
              integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU"
              crossorigin="anonymous">
        <link rel="stylesheet" href="/css/style.css">
        <meta name="theme-color" content="#444444">
    </head>
    <!--
    Ну и фига ты тут забыл ?
    Думаешь найдёшь какую нить дыру ? Ну возможно, а вообще
    Во 1) хуле ты мне сделаешь 2) вовторых пошел нахуй в тетьих 3) что ты мне сделаешь, я в другмо городе.
    за мат извени
     -->
    <body>
    <header>
        <nav class="navbar navbar-expand-lg navbar-light bg-secondary">
            <div class="container">
                <a class="navbar-brand" href="/"><img
                            src="https://bombdash.net/image/logo.png"
                            width="32" height="32" alt="logo"/>BombDash</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target=".navbar-collapse"
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarResponsive">
                    <ul class="navbar-nav">
                        <#assign items =
                        [
                        {
                        "link": "/top",
                        "title": locale.__("global_top")
                        },
                        {
                        "link":"/month",
                        "title": locale.__("month_top")
                        },
                        {
                        "link":"/banlist",
                        "title":locale.__("banlist")
                        },
                        {
                        "link":"/online",
                        "title": locale.__("online_users")
                        },
                        {
                        "link":"/search",
                        "title":locale.__("search")
                        }
                        ]
                        >
                        <#list items as item>
                            <li class="nav-item">
                                <a class="nav-link
                             ${springMacroRequestContext.requestUri?starts_with(item.link)?then("active ","")}"
                                   href="${item.link}">
                                    ${item.title}
                                </a>
                            </li>
                        </#list>
                    </ul>
                    <#if bombDashUser??>
                        <#import "profile.ftl" as profile>
                        <div class="dropdown nav navbar-nav ml-auto" style="user-select: none;">
                            <div id="accountDrop"
                                 data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <table style="margin: 0;border-radius: 0.25rem;"
                                       class="table-prof table round-table bg-info">
                                    <tbody>
                                    <tr>
                                        <@profile.tableRow bombDashUser.getId() bombDashUser.getProfile()/>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="dropdown-menu" aria-labelledby="accountDrop">
                                <a class="dropdown-item" href="/lk">Настройки профиля</a>
                                <a class="dropdown-item" href="/message">Сообщения</a>
                                <a class="dropdown-item" href="/friends">Друзья</a>
                                <a class="dropdown-item" href="/clans">Кланы</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="/logout">Выйти</a>
                            </div>
                        </div>

                    <#else>
                        <ul class="nav navbar-nav ml-auto">
                            <li class="nav-item"><a class="nav-link" href="/lk">Вход</a></li>
                        </ul>
                    </#if>
                </div>
            </div>
        </nav>
    </header>

    <div class="<#if adRandom??>${adRandom} </#if>container content">
        <#nested>
    </div>


    <script src="/js/jquery.min.js"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
            crossorigin="anonymous"></script>
    </body>
</html>
</#macro>
<#macro     jsPost>
    <script>
        $(document).ready(function () { // вся мaгия пoслe зaгрузки стрaницы
            $('form').submit(function () { // пeрeхвaтывaeм всe при сoбытии oтпрaвки
                var form = $(this); // зaпишeм фoрму, чтoбы пoтoм нe былo прoблeм с this
                var error = false; // прeдвaритeльнo oшибoк нeт

                if (!error) { // eсли oшибки нeт
                    var data = form.serialize(); // пoдгoтaвливaeм дaнныe
                    $.ajax({ // инициaлизируeм ajax зaпрoс
                        type: 'POST', // oтпрaвляeм в POST фoрмaтe, мoжнo GET
                        url: String(form.attr('action')), // путь дo oбрaбoтчикa
                        dataType: 'json', // oтвeт ждeм в json фoрмaтe
                        data: data, // дaнныe для oтпрaвки
                        beforeSend: function (data) { // сoбытиe дo oтпрaвки
                            form.find('input[type="submit"]').attr('disabled', 'disabled'); // нaпримeр, oтключим кнoпку, чтoбы нe жaли пo 100 рaз
                        },
                        success: function (data) {
                            if (data['error']) { // eсли oбрaбoтчик вeрнул oшибку
                                form.after('<div class="alert alert-dismissible alert-danger"><button type="button" class="close" data-dismiss="alert">&times;</button>' + data['error'] + '</div>'); // пoкaжeм eё тeкст
                            } else if (data['success']) {
                                form.after('<div class="alert alert-dismissible alert-success"><button type="button" class="close" data-dismiss="alert">&times;</button>' + data['success'] + '</div>');
                                //alert(data['success']);
                            } else { // eсли всe прoшлo oк
                                window.location.href = data['url']; // редиректим
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) { // в случae нeудaчнoгo зaвeршeния зaпрoсa к сeрвeру
                            alert(data + "\r\n" + ajaxOptions + "\r\n" + thrownError);
                        },
                        complete: function (data) { // сoбытиe пoслe любoгo исхoдa
                            form.find('input[type="submit"]').prop('disabled', false); // в любoм случae включим кнoпку oбрaтнo
                        }

                    });
                }
                return false; // вырубaeм стaндaртную oтпрaвку фoрмы
            });
        });
    </script>
</#macro>