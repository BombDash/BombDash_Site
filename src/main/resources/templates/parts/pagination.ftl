<#assign utils=statics['net.bombdash.core.other.Utils']>
<#-- @ftlvariable name="utils" type="net.bombdash.core.other.Utils" -->
<#macro paginate count>
    <#assign first="fa-fast-backward">
    <#assign back="fa-step-backward">
    <#assign next="fa-step-forward">
    <#assign last="fa-fast-forward">
<#-- @ftlvariable name="page" type="java.lang.Integer" -->
    <#assign pageCount =  (count/100)?ceiling>
    <#if pageCount==0>
        <#return >
    </#if>
    <ul style="float:right;" class="pagination">
        <#assign page = utils.getCurrentPage(request)>
        <#assign  chunkSize = 3>
        <#assign chunkStart = page-chunkSize>
        <#assign chunkEnd = page+chunkSize>
        <#--        ВЫРАВНИВАНИЕ-->
        <#assign chunkStart = (chunkStart>0)?then(chunkStart,1)>
        <#assign chunkEnd = (chunkEnd>pageCount)?then(pageCount,chunkEnd)>
        <#--        ВЫРАВНИВАНИЕ-->
        <#if (page==1)>
            <li class="page-item disabled"><span class="page-link"><i class="fas ${first}"></i></span></li>
            <li class="page-item disabled"><span class="page-link"><i class="fas ${back}"></i></span></li>
        <#else>
            <li class="page-item"><a class="page-link" href="${utils.getPage(request,"")}"><i
                            class="fas ${first}"></i></a></li>
            <li class="page-item"><a class="page-link" href="${utils.getPage(request,page-1)}"><i
                            class="fas ${back}"></i></a></li>
        </#if>
        <#--        НАЧАЛО ПАГИНАЦИИ-->
        <#list chunkStart..chunkEnd as i>
        <#if i == page>
            <li class="page-item disabled"><span class="page-link">${i}</span></li>
        <#else>
            <li class="page-item"><a class="page-link" href="${utils.getPage(request,i)}">#{i}</a></li>
        </#if>
        <li class="page-item">
            </#list>
            <#--        КОНЕЦ ПАГИНАЦИИ-->
            <#if page==pageCount>
        <li class="page-item disabled"><span class="page-link"><i class="fas ${next}"></i></span></li>
        <li class="page-item disabled"><span class="page-link"><i class="fas ${last}"></i></span></li>
    <#else>
        <li class="page-item"><a class="page-link" href="${utils.getPage(request,page+1)}"><i
                        class="fas ${next}"></i></a></li>
        <li class="page-item"><a class="page-link" href="${utils.getPage(request,pageCount)}"><i
                        class="fas ${last}"></i></a></li>
        </#if>
    </ul>
</#macro>