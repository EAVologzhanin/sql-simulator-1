<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    Practice list
    <div class="card-columns">
        <#list practices as practice>
            <div class="card my-3">
                <div class="card-body">
                    <h5 class="card-title">${practice.name}</h5>
                    <p class="card-text">TODO: Description</p>
                    <a href="/practice/${practice.id}" class="btn btn-primary">Start</a>
                </div>
            </div>
        <#else>
            No available practice
        </#list>
    </div>
</@c.page>