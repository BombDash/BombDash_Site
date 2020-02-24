<#-- @ftlvariable name="statics" type="freemarker.template.TemplateHashModel" -->
<#-- @ftlvariable name="error" type="java.lang.Exception" -->
<#assign Utils=statics['net.bombdash.core.other.Utils']>
<#import "/parts/page.ftl" as page>
<@page.page locale.__("error_handle")>
    <div class="mb-4 large-font text-center">
        <span class="d-inline-block">${locale.__("error_handle")}</span>
        <#if error.getLocalizedMessage()??><span class="d-inline-block">${error.getLocalizedMessage()}</span></#if>
    </div>
    <div class="p-3 rounded bg-danger">
        <div class="rounded p-3" style="background-color: rgba(0,0,0,0.1);">
            <p>
                ${Utils.traceToString(error)}
                <#--<#list error.getStackTrace() as trace>
                    <li class="list-group-item bg-danger">
                        ${trace.getLineNumber()}:${trace.getClassName()} on method ${trace.getMethodName()}
                    </li>
                </#list>-->
            </p>
        </div>
    </div>
</@page.page>