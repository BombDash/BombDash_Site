<!DOCTYPE html>
<html lang="ru"
      style="-webkit-box-sizing: border-box; box-sizing: border-box; --blue: #375a7f; --indigo: #6610f2; --purple: #6f42c1; --pink: #e83e8c; --red: #E74C3C; --orange: #fd7e14; --yellow: #F39C12; --green: #00bc8c; --teal: #20c997; --cyan: #3498DB; --white: #fff; --gray: #999; --gray-dark: #303030; --primary: #375a7f; --secondary: #444; --success: #00bc8c; --info: #3498DB; --warning: #F39C12; --danger: #E74C3C; --light: #303030; --dark: #adb5bd; --breakpoint-xs: 0; --breakpoint-sm: 576px; --breakpoint-md: 768px; --breakpoint-lg: 992px; --breakpoint-xl: 1200px; --font-family-sans-serif: 'Lato', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol'; --font-family-monospace: SFMono-Regular, Menlo, Monaco, Consolas, 'Liberation Mono', 'Courier New', monospace; font-family: sans-serif; line-height: 1.15; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; -ms-overflow-style: scrollbar; -webkit-tap-highlight-color: transparent;">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <style>
        @media (min-width: 576px) {
            .container {
                max-width: 540px;
            }
        }

        @media (min-width: 768px) {
            .container {
                max-width: 720px;
            }
        }

        @media (min-width: 992px) {
            .container {
                max-width: 960px;
            }
        }

        @media (min-width: 1200px) {
            .container {
                max-width: 1140px;
            }
        }

        @media print {
            *,
            *::before,
            *::after {
                text-shadow: none !important;
                -webkit-box-shadow: none !important;
                box-shadow: none !important;
            }

            a:not(.btn) {
                text-decoration: underline;
            }

            img {
                page-break-inside: avoid;
            }

            p {
                orphans: 3;
                widows: 3;
            }

            @page {
                size: a3;
            }

            body {
                min-width: 992px !important;
            }

            .container {
                min-width: 992px !important;
            }
        }
    </style>
</head>
<body style="-webkit-box-sizing: border-box; box-sizing: border-box; margin: 0; font-family: 'Lato', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol'; font-size: 0.9375rem; font-weight: 400; line-height: 1.5; color: #fff; text-align: left; background-color: #222;">

<div class="container content"
     style="-webkit-box-sizing: border-box; box-sizing: border-box; width: 100%; padding-right: 15px; padding-left: 15px; margin-right: auto; margin-left: auto;">
    <div class="rounded bg-secondary p-3 mt-5"
         style="-webkit-box-sizing: border-box; box-sizing: border-box; background-color: #444; border-radius: 0.25rem; margin-top: 3rem; padding: 1rem;">
        <h1 style="-webkit-box-sizing: border-box; box-sizing: border-box; margin-top: 0; margin-bottom: 0.5rem; font-family: inherit; font-weight: 500; line-height: 1.2; color: inherit; font-size: 3rem;">
            Верификация аккаунта BombDash</h1>
    </div>
    <div class="rounded bg-secondary p-3 mt-3"
         style="-webkit-box-sizing: border-box; box-sizing: border-box; background-color: #444; border-radius: 0.25rem; margin-top: 1rem; padding: 1rem;">
        <p style="-webkit-box-sizing: border-box; box-sizing: border-box; margin-top: 0; margin-bottom: 1rem;">
            Вы получили это письмо, т.к. кто-то указал эту почту для регистрации аккаунта <a href="https://bombdash.net"
                                                                                             style="-webkit-box-sizing: border-box; box-sizing: border-box; color: #00bc8c; text-decoration: none; background-color: transparent; -webkit-text-decoration-skip: objects;">BombDash</a>.
            Если это действительно были Вы, то для завершения регистрации необходимо привязать аккаунт BombSquad:</p>
        <p style="-webkit-box-sizing: border-box; box-sizing: border-box; margin-top: 0; margin-bottom: 1rem;">Создайте
            профиль со следующим ником:</p>
        <div class="mb-3 display-4 rounded bg-info w-100 text-center "
             style="-webkit-box-sizing: border-box; box-sizing: border-box; font-size: 3.5rem; font-weight: 300; line-height: 1.2; background-color: #3498DB; border-radius: 0.25rem; width: 100%; margin-bottom: 1rem; text-align: center;">${code}</div>
        <ul style="-webkit-box-sizing: border-box; box-sizing: border-box; margin-top: 0; margin-bottom: 1rem;">
            <li style="-webkit-box-sizing: border-box; box-sizing: border-box;">Зайдите с этим профилем на сервер <span
                        class="font-weight-bold"
                        style="-webkit-box-sizing: border-box; box-sizing: border-box; font-weight: 700;">BombDash - Account Control</span>
            </li>
            <li style="-webkit-box-sizing: border-box; box-sizing: border-box;">Если привязка будет успешна, то вы
                получите уведомление об этом, и вас выкинет с
                сервера
            </li>
            <li style="-webkit-box-sizing: border-box; box-sizing: border-box;">Если привязка провалилась, вы увидите
                сообщение с пояснением
            </li>
        </ul>
        <div class="text-center mt-3"
             style="-webkit-box-sizing: border-box; box-sizing: border-box; margin-top: 1rem; text-align: center;">
            <img class="rounded mb-3 img-fluid" src="http://dev.bombdash.net/profile_window/${code}"
                 style="-webkit-box-sizing: border-box; box-sizing: border-box; vertical-align: middle; border-style: none; max-width: 100%; height: auto; border-radius: 0.25rem; margin-bottom: 1rem;">
            <span class="text-muted" style="-webkit-box-sizing: border-box; box-sizing: border-box; color: #999;">Созданный профиль должен выглядеть так (персонаж и цвета не важны, только имя профиля)</span>
        </div>

    </div>
    <div class="text-center my-4"
         style="-webkit-box-sizing: border-box; box-sizing: border-box; margin-top: 1.5rem; margin-bottom: 1.5rem; text-align: center;">
        <span class="text-muted" style="-webkit-box-sizing: border-box; box-sizing: border-box; color: #999;">Если Вы не регистрировали аккаунт BombDash, то просто проигнорируйте это письмо</span>
    </div>

</div>
</body>
</html>
