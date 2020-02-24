<#-- @ftlvariable name="types" type="java.util.List<String>" -->
<#-- @ftlvariable name="banlist" type="net.bombdash.core.api.models.PlayerList<net.bombdash.core.api.models.BanInfo>" -->
<#import "/parts/page.ftl" as page>
<#import "/parts/profile.ftl" as profileUtils>
<@page.page locale.__("banlist")>
    <h1>${locale.__("banlist")}</h1>
    <div class="mb-3 borderless table-responsive round-table">
        <table class="table table-borderless bg-secondary table-striped">
            <thead class="bg-info hide">
            <tr class="infotable bg-info" style="border-radius: 15px">
                <th class="icon text-center align-middle"></th>
                <th class="align-middle">${locale.__("nick")}</th>
                <th class="align-middle">${locale.__("reason")}</th>
                <th class="align-middle">${locale.__("operator")}</th>
            </tr>
            </thead>
            <tbody>
            <#list banlist.getPlayers() as row>
                <#assign data = row.getData()>
                <tr>
                    <@profileUtils.clickableTableRow row.getId() row.getProfile()/>
                    <td>${data.getReason()}</td>
                    <td>${data.getTextOperator()}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
    <#import "../parts/pagination.ftl" as pagination>
    <@pagination.paginate banlist.getCount()/>
</@page.page>