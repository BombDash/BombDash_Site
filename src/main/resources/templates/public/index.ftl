<#import "/parts/page.ftl" as page>
<@page.page title>
    <div class="mb-4 large-font text-center">
        <span class="d-inline-block">${locale.__("index_welcome")}</span>
    </div>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <img class="w-100" src="/image/full_logo.png" alt="BombDash full logo">
        </div>
    </div>
    <div class="mb-4 large-font text-center">
        <span class="d-inline-block">${locale.__("index_find")}</span>
    </div>
    <div class="row justify-content-center">
        <#list locale.__("index") as card>
            <div class="col-lg-3 mb-4">
                <div class="card card-hover h-100" onclick="location.href='${card.url}';"
                     style="cursor: pointer;">
                    <img style="width: 100%;" src="${card.image}"
                         alt="Posts image">
                    <div class="card-body">
                        <h4 class="card-title">${card.title}</h4>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</@page.page>