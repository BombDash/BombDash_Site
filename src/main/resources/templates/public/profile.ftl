<#-- @ftlvariable name="response" type="net.bombdash.core.api.methods.player.get.PlayerGetResponse" -->
<#import "/parts/page.ftl" as page>
<#macro listScores map global>
<#-- @ftlvariable name="map" type="java.util.Map<java.lang.String,java.lang.Integer>" -->
    <#list map?keys as key>
        <tr>
            <td style="width: 1px"><a target="_blank" href="/placeRedirect/${global?then("true","false")}/${key}/${id}"><img
                            class="text-center" src="/image/cup.png" height="28"></a></td>
            <td>${locale.__("name_"+key)}</td>
            <td>${map[key]}</td>
        </tr>
    </#list>
</#macro>
<#assign utils = statics["net.bombdash.core.other.Utils"]>
<#if response??>
    <#assign title = locale.__("player_profile")?replace("{name}",response.getLastProfile().getName())>
<#else >
    <#assign title = locale.__("player_not_found")>
</#if>
<@page.page title>
    <style>
        .table > tbody > tr > td {
            vertical-align: middle;
        }
    </style>
    <#if response??>
        <#import "/parts/profile.ftl" as profileUtils>
        <#assign last = response.getLastProfile()>
        <#assign currentTimeStamp = .now?long/1000 >
        <div class="bg-secondary bgSection p-3">
            <div class="textTransfer">
                <h1 class="profileName">Статистика игрока <@profileUtils.playerNickname last/></h1>
                <a href="/" class="profile-button btn btn-success" role="button"
                   aria-pressed="true">Купить привилегию</a></div>
            <#if response.getBan()??>
                <table class="round-table mt-3 table bg-light">
                    <tbody>
                    <tr class="text-center bg-danger">
                        <td colspan="2"><span>Забанен</span></td>
                    </tr>
                    <tr class="text-center">
                        <td>${locale.__("reason")}</td>
                        <td>${response.getBan().getReason()}</td>
                    </tr>
                    <tr class="text-center">
                        <td>${locale.__("operator")}</td>
                        <td>
                            <#if response.getBan().getTextOperator()=="CONSOLE">
                            <span>CONSOLE</span>
                            <#else >
                            <a href="/profile/${response.getBan().getTextOperator()}">${response.getBan().getTextOperator()}
                                </#if>

                        </td>
                    </tr>
                    <tr class="text-center">
                        <td>${locale.__("ban_end")}</td>
                        <td><#if response.getBan().getEnd()??>
                                через ${utils.getLeftTime(locale,response.getBan().getEnd() - currentTimeStamp)}
                            <#else>
                                ${locale.__("ban_forever")}
                            </#if></td>
                    </tr>
                    </tbody>
                </table>
            </#if>
            <div class="section2">
                <div class="profileIcon">
                    <@profileUtils.iconImage last 256 256/>
                </div>
                <table class="table table-prof bg-light">
                    <tbody>
                    <tr>
                        <td>${locale.__("status")}</td>
                        <#assign different = currentTimeStamp-response.getLastPing() >
                        <td><#if (different<60*5)>
                                <span class="text-success">${locale.__("online")}</span>
                            <#else>
                                <span class="text-dark">
                                    ${locale.__("was_online")}
                                    ${utils.getLeftTime(locale,currentTimeStamp-response.getLastPing())}
                                    ${locale.__("back")}</span>
                            </#if></td>
                    </tr>
                    <#if response.getLastServer()??>
                        <tr>
                            <td>${locale.__("last_server")}</td>
                            <td>${response.getLastServer()}</td>
                        </tr>
                    </#if>
                    <tr>
                        <td>${locale.__("privilege")}</td>
                        <td>${locale.__("prv_"+response.getStatus()???then(response.getStatus(),"player"))}</td>
                    </tr>

                    <tr>
                        <td>${locale.__("first_play")}</td>
                        <td>${utils.getFormattedDate(response.getFirstPlay())}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="bgSection bg-secondary p-3">
            <div class="textTransfer mb-3">
                <span class="profileName">${locale.__("score")}</span>
            </div>
            <ul class="nav nav-pills">
                <li class="nav-item mr-2">
                    <a class="nav-link active" data-toggle="tab" href="#score_all">${locale.__("score_all")}</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="#score_month">${locale.__("score_month")}</a>
                </li>
            </ul>
            <div class="tab-content">

                <div class="tab-pane show active" id="score_all">
                    <div class="table-i-div">
                        <table class="table round-table bg-light">
                            <tbody>
                            <@listScores response.getScore() true/>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="tab-pane" id="score_month">
                    <div class="table-i-div">
                        <table class="table round-table bg-light">
                            <tbody>
                            <@listScores response.getMonthScore() false/>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="bgSection bg-secondary p-3">
            <div class="textTransfer mb-3">
                <span class="profileName">${locale.__("player_profiles")}</span>
            </div>
            <div class="table-i-div">
                <table class="round-table table bg-light">
                    <tbody>
                    <#list response.getProfiles() as profile>
                        <tr>
                            <td class="icon text-center"><@profileUtils.iconImage profile 64 40/></td>
                            <td class="nickname"><@profileUtils.playerNickname profile/></td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="bgSection bg-secondary p-3">
            <div class="textTransfer mb-3">
                <span class="profileName">${locale.__("game_history")}</span>
            </div>
            <div class="table-i-div">
                <table class="round-table table bg-light">
                    <tbody>
                    <#list response.getGames() as game>
                        <tr>
                            <td class="bg-info" colspan="2">
                                <span>${game.getServer()}</span>
                                <span class="text-right">${utils.getFormattedDate(game.getTime())}</span>
                            </td>
                        </tr>
                        <#list game.getPlayers()?keys as playerId>
                            <tr>
                                <@profileUtils.clickableTableRow playerId game.getPlayers()[playerId]/>
                            </tr>
                        </#list>

                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    <#else>
        <div class="bg-secondary bgSection">
            <div class="textTransfer m,">

                <span class="profileName">${title}</span>
            </div>
        </div>
    </#if>
</@page.page>