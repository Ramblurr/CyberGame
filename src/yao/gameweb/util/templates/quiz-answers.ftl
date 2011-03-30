<#include "header.ftl">
<div class="grid_9">
<p>Hello ${username}! Please answer the questions below.</p>

<form action="/quiz/answer" method="post">
        <div class="form-item">
            <#list questions as question >
                <p><strong>${question.text}</strong>
                <ul>
                <#list question.answers as answer>
                <input type="hidden" name="question_${question.id}" value="${question.id}" />
                <li>
                <#if answer.correct == 1> 
                	<label class="label_radio correct" for="ans${answer.id}">
                        <input name="questions_${question.id}" id="ans${answer.id}" value="${answer.id}" type="radio" checked/>
                    ${answer.text}
                    </label>
                <#elseif answer.correct == -1>
                	<label class="label_radio incorrect" for="ans${answer.id}">
                        <input name="questions_${question.id}" id="ans${answer.id}" value="${answer.id}" type="radio" checked/>
                    ${answer.text}
                    </label>
                <#else>
                    <label class="label_radio" for="ans${answer.id}">
                        <input name="questions_${question.id}" id="ans${answer.id}" value="${answer.id}" type="radio" />
                    ${answer.text}
                    </label>
                </#if>
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