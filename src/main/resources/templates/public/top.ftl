<#-- @ftlvariable name="type" type="java.lang.String" -->
<#-- @ftlvariable name="types" type="java.util.List<String>" -->
<#-- @ftlvariable name="top" type="net.bombdash.core.api.models.PlayerList<net.bombdash.core.api.methods.stats.get.StatsGet.StatsGetData>" -->
<#import "/parts/page.ftl" as page>
<#import "/parts/profile.ftl" as profileUtils>
<@page.page locale.__("top_"+type)>
    <h1>${locale.__("top_"+type)}</h1>
    <select class="browser-default custom-select mb-3"
            onchange="location ='/${global?then("top","month")}/'+this.value">
        <#list types as topType>
            <option <#if type == topType >selected </#if>value="${topType}">${locale.__("top_"+topType)}</option>
        </#list>
    </select>
    <div class="mb-3 borderless table-responsive round-table">
        <table class="table table-borderless bg-secondary table-striped">
            <thead class="bg-info hide">
            <tr class="top-info table-info" style="border-radius: 15px">
                <th style="width: 1%" class="text-center align-middle"></th>
                <th class="text-center icon-size align-middle"></th>
                <th class="align-middle">${locale.__("nick")}</th>
                <th class="align-middle score text-center">${locale.__("name_"+type)}</th>
            </tr>
            </thead>
            <tbody>
            <#assign glow = request.getParameter("glow")???then(request.getParameter("glow"),"")>
            <#list top.getPlayers() as row>
                <#if glow == row.getId()>
                    <tr class="bg-danger" id="scrollToMe">
                <#else>
                    <tr>
                </#if>
                <td class="text-center hide">${row.getPlace()}</td>
                <@profileUtils.clickableTableRow row.getId() row.getProfile()/>
                <td class="text-center">${row.getData().getScore()}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
    <#import "../parts/pagination.ftl" as pagination>
    <@pagination.paginate top.getCount()/>
</@page.page>
<#if glow??>
    <script>
        $(document).ready(function () {
            let me = document.getElementById("scrollToMe");
            if (me != null)
                me.scrollIntoView({block: "center", behavior: "smooth"});
        });
    </script>
</#if>