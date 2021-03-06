<header> <jsp:include page="header.jsp" /> </header>
<form id="oauthClientRegisterForm" action="/oauth/client-register/process" method="post">
    <div class="container">
        <div class="row">
            <div class="col-md-4 col-md-push-4">
                <div class="form-group">
                    <label for="email">Username: <span title="Enter the email you registered as a user">(?)</span></label>
                    <input name="email" id="email"
                                class="form-control" value="user1" autofocus required/>
                </div>
                <div class="form-group">
                    <label for="password">Password: <span title="Enter your correct account password">(?)</span></label>
                    <input type="password" name="password" id="password" class="form-control" value="123456" required/>
                </div>
                <div class="form-group">
                    <label for="app-name">App name:</label>
                    <input name="app_name" id="app-name" class="form-control" value="user app" required/>
                </div>
                <div class="form-group">
                    <label for="client-uri">Client URI:</label>
                    <input name="client_uri" id="client-uri" class="form-control" value="http://localhost:8081" required/>
                </div>
                <div class="form-group">
                    <label for="redirect-uri">Redirect link:</label>
                    <input name="redirect_uri" id="redirect-uri" class="form-control" value="http://localhost:8081/loginByOauth" required/>
                </div>
                <div class="form-group">
                    <label for="grant-types">Grant types: <span title="rg.apache.oltu.oauth2.common.message.types.GrantType">(?)</span>/label>
                    <select name="grant_types" id="grant-types" class="form-control">
                        <option value="authorization_code" selected>Authorization code</option>
                        <option value="implicit">Implicit</option>
                        <option value="password">Password</option>
                        <option value="refresh_token">Refresh token</option>
                        <option value="client_credentials">Client credential</option>
                    </select>
                </div>
                <button id="register" class="btn btn-primary" name="login">Register</button>
            </div>
        </div>
    </div>

</form>

<footer><jsp:include page="footer.jsp" /></footer>
