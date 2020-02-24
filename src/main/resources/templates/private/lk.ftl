<#import "/parts/page.ftl" as page>
<@page.page locale.__("personal_area")>
    <div class="mx-0 row">
        <div id="button-panel" class="col-lg-3 mr-lg-3 mb-3 mb-lg-0 rounded bg-secondary">
            <a href="#prefix">
                <button class="btn btn-success w-100 my-3">Настройка префикса</button>
            </a>
            <a href="#particle">
                <button class="btn btn-warning w-100 mb-3">Настройка партиклов</button>
            </a>
            <a href="#profile">
                <button class="btn btn-danger w-100 mb-3">Настройка профиля</button>
            </a>
        </div>
        <div class="p-3 col-lg rounded bg-secondary">
            <div class="tab-content">
                <div id="prefix" class="tab-pane fade">
                    <h3>Настройка префикса</h3>
                    <p>Скоро здесь можно будет настроить префикс.</p>
                </div>
                <div id="particle" class="tab-pane fade">
                    <h3>Настройка партиклов</h3>
                    <p>Скоро здесь можно будет настроить партиклы.</p>
                </div>
                <div id="profile" class="tab-pane fade">
                    <h3>Настройка профиля</h3>
                    <p>Скоро здесь можно будет настроить профиль.</p>
                </div>
            </div>
        </div>
    </div>
</@page.page>
<script>
    $(document).ready(function () {
        $("#button-panel a").click(function () {
            $("#button-panel .active").removeClass("active");
            $(this).tab('show');
        });
    });
</script>