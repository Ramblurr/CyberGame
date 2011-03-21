<#include "header.ftl">
<div class="grid_9">
<p>Hello ${username}! Please answer the questions below.</p>

<form action="/quiz/answer" method="post">
        <div class="form-item">
            <#list questions as question >
                <p>${question.text}
                <ul>
                <#list question.answers as answer>
                <li>
                    <label class="label_radio" for="ans${answer.id}">
                        <input name="ans${answer.id}" id="ans${answer.id}" value="${answer.id}" type="radio" />
                    ${answer.text}
                    </label>
                </li>
                </#list>
                </ul>
                </p>
            </#list>
        </div>
        <div class="form-item submit-wrap"><input type="submit" id="submit" class="button" value="Submit" /></div>
</form>

</div>
<#include "footer.ftl">
<div class="clear"></div>