<#-- @ftlvariable name="title" type="String" -->
<#import "/parts/page.ftl" as page>
<#import "/parts/profile.ftl" as profileUtils>
<#assign title = locale.__("online_users")>
<@page.page title>
    <h1>${title}</h1>
    <#list map?keys as key>
        <div class="textTransfer mb-3">
            <span class="profileName">${key}</span>
        </div>
        <div class="mb-3 borderless table-responsive round-table">
            <table class="table table-borderless bg-secondary table-striped">
                <thead class="bg-info hide">
                <tr class="top-info table-info" style="border-radius: 15px">
                    <th class="text-center icon-size align-middle"></th>
                    <th class="align-middle">${locale.__("nick")}</th>
                </tr>
                </thead>
                <tbody>
                <#assign value=map[key]>
                <#list value.getPlayers() as row>
                    <tr>
                        <@profileUtils.clickableTableRow row.getId() row.getProfile()/>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    <#else>
        <div class="p-3 bg-secondary">
            <div class="textTransfer mb-3">
                <span class="profileName">${locale.__("empty_servers")}</span>
            </div>
        </div>
    </#list>
</@page.page>