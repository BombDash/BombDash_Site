<#-- @ftlvariable name="info" type="net.bombdash.core.api.methods.player.get.PlayerGetResponse" -->
<#import "/parts/page.ftl" as page>
<#assign Utils=statics['net.bombdash.core.other.Utils']>
<@page.page locale.__("personal_area")>
    <div class="mx-0 row">
        <div id="button-panel" class="col-lg-3 mr-lg-3 mb-3 mb-lg-0 rounded bg-secondary">
            <a href="#server">
                <button class="btn btn-success w-100 my-3">Настройка отображения на сервере</button>
            </a>
            <a href="#profile">
                <button class="btn btn-danger w-100 mb-3">Настройка профиля</button>
            </a>
        </div>
        <div class="p-3 col-lg rounded bg-secondary">
            <div class="tab-content">
                <div id="server" class="tab-pane fade active show">
                    <form class="form-horizontal" method="post" action="/form/account_serverSetting">
                        <h3>Настройка префикса</h3>
                        <#assign prefixIsset = info?? && info.getPrefix()?? && info.getPrefix().getText()??>
                        <div class="col-lg-4 pt-3 mb-3 round-table" style="background: rgba(0, 0, 0, 0.2);">
                            <div class="form-group">
                                <label class="control-label" for="prefix">Префикс</label>
                                <div>
                                    <input id="prefix" name="prefix" type="text"
                                            <#if prefixIsset>
                                                value="${info.getPrefix().getText()}"
                                            </#if>
                                           placeholder="Введите сюда префикс"
                                           class="form-control input-md">

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label" for="speed">Скорость смены цветов префикса</label>
                                <div>
                                    <input class="slider" type="range" name="speed" id="speed" min="0" max="1000"
                                           value="${prefixIsset?then(info.getPrefix().getSpeed(),250)}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label" for="speed">Цвета</label>
                                <div id="color_append">
                                    <#if prefixIsset>
                                        <#assign k = 0>
                                        <#list info.getPrefix().getAnimation() as color>
                                            <#assign k = k+1>
                                            <div <#if k==1>id="main_color"</#if> class="input-group mb-3">
                                                <input type="color" class="form-control" name="color${k}"
                                                       value="${Utils.rgbToHex(color)}">
                                                <div class="input-group-append">
                                                    <button type="button" <#if k==1>disabled</#if>
                                                            class="delete_button btn input-group-text"><i
                                                                class="fas fa-times"></i></button>
                                                </div>
                                            </div>
                                        </#list>
                                    <#else>
                                        <div id="main_color" class="input-group mb-3">
                                            <input type="color" class="form-control" name="color1" value="#ff0000">
                                            <div class="input-group-append">
                                                <button type="button" disabled
                                                        class="delete_button btn input-group-text"><i
                                                            class="fas fa-times"></i></button>
                                            </div>
                                        </div>
                                    </#if>

                                </div>
                                <div>
                                    <button type="button" id="add_color" class="w-100 btn btn-success">Добавить цвет
                                    </button>
                                </div>
                            </div>
                        </div>
                        <h3>Настройка партиклов</h3>
                        <div class="col-lg-4 pt-3 mb-3 round-table" style="background: rgba(0, 0, 0, 0.2);">
                            <div class="form-group">
                                <label class="control-label" for="particle_type">Тип партиклов</label>
                                <div>
                                    <select id="particle_type" name="particle_type" class="form-control">
                                        <#assign  particleTypes = [
                                        {
                                        "type":"spark",
                                        "name":"Огонь"
                                        },
                                        {
                                        "type":"splinter",
                                        "name":"Осколок"
                                        },
                                        {
                                        "type":"sweat",
                                        "name":"Пар"
                                        },
                                        {
                                        "type":"rock",
                                        "name":"Камень"
                                        },
                                        {
                                        "type":"slime",
                                        "name":"Слизь"
                                        },
                                        {
                                        "type":"metal",
                                        "name":"Метал"
                                        },
                                        {
                                        "type":"ice",
                                        "name":"Лёд"
                                        }
                                        ]>
                                        <#assign activeType = info.getParticle()???then(info.getParticle().getParticleType(),"spark")>
                                        <#list particleTypes as type>

                                            <option <#if type['type']==activeType>selected</#if>
                                                    value="${type['type']}">${type['name']}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label" for="particle">Тип партиклов</label>
                                <div>
                                    <#assign emitTypes = [
                                    {
                                    "value":"off",
                                    "name":"Отключить частицы"
                                    },
                                    {
                                    "value":"body",
                                    "name":"Из тела"
                                    },
                                    {
                                    "value":"legs",
                                    "name":"В ногах"
                                    },
                                    {
                                    "value":"around",
                                    "name":"Вокруг персонажа"
                                    },
                                    {
                                    "value":"underfoot",
                                    "name":"Под ногами"
                                    }
                                    ]>
                                    <select id="particle" name="emit_type" class="form-control">
                                        <#assign activeEmit = info.getParticle()???then(info.getParticle().getEmitType(),"body")>
                                        <#list emitTypes as type>
                                            <option <#if type['value']==activeEmit>selected</#if>
                                                    value="${type['value']}">${type['name']}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>

                        </div>
                        <div class="col-lg-4 px-0">
                            <input type="submit" value="Сохранить" class="w-100 btn btn-success">
                        </div>
                    </form>
                </div>
                <div id="profile" class="tab-pane fade">
                    <h3>Настройка профиля</h3>
                    <p>Скоро здесь можно будет настроить профиль.</p>
                </div>
            </div>
        </div>
    </div>
</@page.page>
<@page.jsPost />
<script>

    $(document).ready(function () {
        $("#button-panel a").click(function () {
            $("#button-panel .active").removeClass("active");
            $(this).tab('show');
        });

        let i = <#if prefixIsset>
                ${info.getPrefix().getAnimation().size()?c}
                <#else>
                1
            </#if>;

        function recheckColors() {
            let children = $("#color_append").children();
            i = children.length;
            for (let k = 1; k < i; k++) {
                let current = children[k];
                $(current).children("input").attr("name", "color" + (k + 1).toString());
            }
            $("#add_color").attr("disabled", false);
        }

        let deleteFunc = function () {
            $(this).parent().parent().remove();
            recheckColors();
        };
        $(".delete_button").click(
            deleteFunc
        );
        $("#add_color").click(function () {
            i++;
            let colorAppend = $("#color_append");
            let element = $("#main_color").clone();
            element.removeAttr('id');
            element.children("input").attr("name", "color" + i.toString());
            let button = element.find("button");
            button.attr("disabled", false);
            button.click(deleteFunc);
            element.appendTo(colorAppend);
            if (i >= 8) {
                $(this).attr("disabled", true);
            }
        });

    });
</script>