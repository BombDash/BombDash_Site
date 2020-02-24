<#-- @ftlvariable name="nick" type="java.lang.String" -->
<#-- @ftlvariable name="list" type="net.bombdash.core.api.models.PlayerList<java.util.List<net.bombdash.core.api.models.PlayerProfile>>" -->
<#import "/parts/page.ftl" as page>
<#import "/parts/profile.ftl" as profileUtils>
<#macro textArea text>
    <div class="bg-secondary bgSection">
        <div class="mydiv" style="text-align: center;">
            <span style="font-size:2rem;">${text}</span>
        </div>
    </div>
</#macro>
<@page.page locale.__("search")>
    <form method="get" action="/search" style="margin-bottom: 1rem;">
        <div class="search form-row">
            <div class="col" style="    max-width: unset; width: 0;">
                <input type="text" name="q" class="form-control" title="Введите искомого игрока"
                        <#if nick??>
                            value="${nick}"
                        </#if>
                       placeholder="Введите ник">
            </div>
            <div class="col" style="display: inline-block;">
                <input type="submit" class="btn btn-primary" value="Найти">
            </div>
        </div>
    </form>
    <#if list??>
        <#list list.getPlayers() as player>
            <div class="bgSection p-3 bg-secondary">
                <div class="nickname textTransfer mb-3">
                    <span class="profileName"><@profileUtils.clickablePlayerNickName player.getId() player.getProfile()/></span>
                </div>
                <table class="bg-light table round-table">
                    <#list player.getData() as profile>
                        <tr>
                            <@profileUtils.clickableTableRow player.getId() profile/>
                        </tr>
                    </#list>
                </table>
            </div>
        <#else>
            <@textArea locale.__("search_not_found")/>
        </#list>
    <#elseif (nick?length == 0) >
        <@textArea locale.__("search_input")/>
    <#elseif (nick?length <= 3) >
        <@textArea locale.__("search_small")/>
    </#if>

</@page.page>