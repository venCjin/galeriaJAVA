<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="components/page_head.jsp">
  <jsp:param name="title" value="JAVA - reg-log"/>
</jsp:include>

<jsp:include page='components/nav.jsp'>
  <jsp:param name="navID" value="reg-log"/>
</jsp:include>

<% String log_err = request.getParameter("log_err");
  if(log_err!=null) {%>
    <p class="error" style="width: 100%; text-align:center;"><i style="color: #EB3232;" class="fa fa-exclamation-circle" aria-hidden="true"></i> Aby dodawać albumy i zdjęcia muszisz być zalogowany</p>
    <br/>
<%} %>

<section>

  <div class="reg-log">
    <div id="reg">

      <h2>Rejestracja</h2>

      <form method="post" action="regiester">
        <div class="input-group margin-bottom-sm">
          <span class="input-group-addon"><i class="fa fa-user fa-fw" aria-hidden="true"></i></span>
          <input class="form-control" minlength="6" maxlength="20" type="text" name="login" value="${param.login}" placeholder="Login" required>
        </div>

        <div class="input-group margin-bottom-sm">
          <span class="input-group-addon"><i class="fa fa-key fa-fw" aria-hidden="true"></i></span>
          <input class="form-control" minlength="6" maxlength="20" type="password" name="password" placeholder="Hasło" required>
        </div>

        <div class="input-group margin-bottom-sm">
          <span class="input-group-addon"><i class="fa fa-key fa-fw" aria-hidden="true"></i></span>
          <input class="form-control" minlength="6" maxlength="20" type="password" name="password2" placeholder="Hasło (ponownie)" required>
        </div>

        <div class="input-group margin-bottom-sm">
          <span class="input-group-addon"><i class="fa fa-envelope-o fa-fw" aria-hidden="true"></i></span>
          <input class="form-control" maxlength="128" type="email" name="email" value="${param.email}" placeholder="Email" required>
        </div>
        <input type="submit" value="Zarejestruj">
      </form>

      <c:forEach items="${requestScope.R_errors}" var="error">
        <p class="error"><i style="color: #EB3232;" class="fa fa-exclamation-circle" aria-hidden="true"></i> ${error}</p>
      </c:forEach>

    </div>

    <div id="log">

      <h2>Logowanie</h2>

      <form method="post" action="login">
        <div class="input-group margin-bottom-sm">
          <span class="input-group-addon"><i class="fa fa-user fa-fw" aria-hidden="true"></i></span>
          <input class="form-control" type="text" name="L_login" placeholder="Login" required>
        </div>

        <div class="input-group margin-bottom-sm">
          <span class="input-group-addon"><i class="fa fa-key fa-fw" aria-hidden="true"></i></span>
          <input class="form-control" type="password" name="L_password" placeholder="Hasło" required>
        </div>

        <input type="submit" value="Zaloguj">
      </form>

      <c:if test = "${requestScope.L_error != null}">
        <p class="error"><i style="color: #EB3232;" class="fa fa-exclamation-circle" aria-hidden="true"></i> ${L_error}</p>
      </c:if>
    </div>
  </div>

  <div style="clear:both;"></div>

</section>

<jsp:include page="components/page_footer.jsp"/>