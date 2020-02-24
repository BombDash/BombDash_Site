<#import "/parts/page.ftl" as page>
<@page.page "Вход">
    <style>
        .form {
            width: 50%;
            margin: 0 auto;
        }

        @media (max-width: 576px) {
            .form {
                width: 100%;
            }
        }

        .input-group {
            width: 90%;
            margin: auto auto 1rem;
        }

        .bgSectionLogin {
            border-radius: 0 15px 15px 15px;
        }


        .nav-tabs {
            border-bottom: unset;
        }

        .form-control:focus {
            border-color: #66afe9;
        }
    </style>
    <div class="form">
        <div class="" id="loginModal">
            <div class="modal-body">
                <div class="well">
                    <ul class="nav nav-tabs">
                        <li class="nav-item">
                            <a class="nav-link active show" data-toggle="tab" href="#login">Вход</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" data-toggle="tab" href="#create">Регистрация</a>
                        </li>
                    </ul>
                    <div id="myTabContent" class="tab-content">
                        <div class="tab-pane active in" id="login">
                            <form class="bg-light bgSectionLogin form-horizontal" role="form" method="POST"
                                  action="/login">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <span class="mt-3 input-group-text"><i class="fas fa-envelope"></i></span>
                                    </div>

                                    <input type="email" class="form-control mt-3" name="email" placeholder="Email">
                                </div>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text"><i class="fas fa-lock"></i></span>
                                    </div>
                                    <input type="password" name="password" class="form-control" placeholder="Пароль">
                                </div>
                                <div class="input-group" style="margin-bottom: 0">
                                    <input type="submit" value="Войти" class="btn btn-success mb-3" style="width: 100%">
                                </div>
                            </form>
                        </div>
                        <div class="tab-pane fade" id="create">
                            <form class="bg-light bgSectionLogin form-horizontal" role="form" method="POST"
                                  action="/form/account_create">
                                <div class="input-group">
                                    <div class="input-group-prepend mt-3">
                                        <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                                    </div>
                                    <input type="email" required="" name="email" class="mt-3 form-control"
                                           placeholder="Email">
                                </div>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text"><i class="fas fa-lock"></i></span>
                                    </div>
                                    <input type="password" required="" name="password" class="form-control"
                                           placeholder="Пароль">
                                </div>
                                <div class="input-group" style="margin-bottom: 0">
                                    <input type="submit" id="submit_btn" value="Зарегистрироваться"
                                           class="mb-3 btn btn-warning" style="width: 100%">
                                </div>
                                <input type="hidden" name="reg" value="1">
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@page.page>
<@page.jsPost />
