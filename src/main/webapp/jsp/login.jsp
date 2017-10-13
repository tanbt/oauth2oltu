<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<header> <jsp:include page="header.jsp" /> </header>
<%--@elvariable id="Login" type="com.tanbt.oauth2oltu.entity.Login"--%>
<form:form id="loginForm" modelAttribute="login" action="loginProcess" method="post">
   <div class="container">
       <div class="row">
           <div class="col-md-4 col-md-push-4">
               <div class="form-group">
                   <form:label path="email">Username: </form:label>
                   <form:input path="email" name="email" id="email"
                               class="form-control" placeholder="Enter email"/>
               </div>
               <div class="form-group">
                   <form:label path="password">Password:</form:label>
                   <form:password path="password" name="password" id="password" class="form-control" placeholder="Password"/>
               </div>
               <form:button id="login" class="btn btn-primary" name="login">Login</form:button>
           </div>
       </div>
   </div>

</form:form>

<footer><jsp:include page="footer.jsp" /></footer>
