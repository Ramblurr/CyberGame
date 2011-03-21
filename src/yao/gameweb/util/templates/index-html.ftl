<#include "header.ftl">
<div class="grid_8">
<p>Hello! Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam malesuada placerat scelerisque. Nullam pellentesque aliquam risus nec venenatis. Morbi a posuere sapien. Suspendisse adipiscing odio vitae massa facilisis ac dictum enim dictum. Mauris luctus gravida dolor auctor molestie. Sed sollicitudin, odio dignissim dapibus dapibus, orci risus lacinia neque, quis molestie eros nunc eu ante. Donec pharetra, lorem a tempor laoreet, erat dolor varius augue, vel commodo magna metus in nisl. Nunc suscipit elementum accumsan.
</p>
</div>
<div class="grid_4">
<div class="login-block">
    <h3>Get Started!</h3>
    <form action="/login" method="post">
        <div class="form-item">
            <label for="secret-user-name">Username:</label>
            <input type="text" name="user-name" id="user-name" />
        </div>
        <!---<div class="form-item"><label for="secret-password">Password:</label><input type="password" name="password" id="password" /></div> -->
        <div class="form-item submit-wrap"><input type="submit" id="submit" class="button" value="Login" /></div>
</form>
</div>
</div>
<div class="clear"></div>
<#include "footer.ftl">