<#setting url_escaping_charset="UTF-8">
<#macro iconImage profile , size,htmlSize = size>
    <img class="text-center icon-img-small"
         src="/profile_photo/${profile.getCharacter()}/${size?c}/${profile.getColor()?c}/${profile.getHighlight()?c}"
         height="${htmlSize?c}" alt="">
</#macro>

<#macro clickableIcon id profile size,htmlSize>
    <a href="/profile/${id?url}">
        <@iconImage profile,size,htmlSize />
    </a>
</#macro>
<#macro playerNickname profile>
<#-- @ftlvariable name="profile" type="net.bombdash.core.api.models.PlayerProfile" -->
    <#if profile.needIcon()><img class="small-icon" src="/icon/${profile.getIcon()?url}.png" alt=""
                                 width="16"></#if>${profile.getName()}
</#macro>

<#macro clickablePlayerNickName id profile>
    <a href="/profile/${id?url}">
        <@playerNickname profile/>
    </a>
</#macro>

<#macro clickableTableRow id profile>
    <td class="icon text-center"><@clickableIcon id profile,64,40/>
    <td class="nickname"><@clickablePlayerNickName id profile /></td>
</#macro>
<#macro tableRow id profile>
    <td class="icon text-center"><@iconImage profile,64,40/>
    <td class="nickname"><@playerNickname profile /></td>
</#macro>