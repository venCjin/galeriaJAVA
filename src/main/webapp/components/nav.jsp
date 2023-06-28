<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<nav>
  <a id="galeria" class='button' href='/'><i class='fa fa-home'></i> Galeria</a>

<%--  <a id="top-foto" class='button' href='/top-foto.jsp'><i class='fa fa-star'></i> Najlepiej oceniane</a>--%>

<%--  <a id="nowe-foto" class='button' href='/nowe-foto.jsp'><i class='fa fa-certificate'></i> Najnowsze</a>--%>

  <c:choose>

    <c:when test = "${user != null}">
      <a id='add-a' class='button' href='/dodaj-album'><i class='fa fa-folder' aria-hidden='true'></i> Załóż album</a>
<%--      <a id='add-z' class='button' href='/dodaj-foto'><i class='fa fa-plus-square' aria-hidden='true'></i> Dodaj zdjęcie</a>--%>
<%--      <a id='acc' class='button' href='/konto'><i class='fa fa-user fa-fw' aria-hidden='true'></i> Moje konto</a>--%>

      <c:if test = "${user.isAdmin() || user.isMod()}">
        <a class='button' href='/admin'><i class='fa fa-unlock-alt' aria-hidden='true'></i> Panel admina</a>
      </c:if>

      <a class='button' href='/logout'><i class='fa fa-sign-out' aria-hidden='true'></i> Wyloguj się</a>
    </c:when>

    <c:otherwise>
      <a class='button' href='/reg-log.jsp?log_err'><i class='fa fa-folder' aria-hidden='true'></i> Załóż album</a>
<%--      <a class='button' href='/reg-log.jsp?log_err'><i class='fa fa-plus-square' aria-hidden='true'></i> Dodaj zdjęcie</a>--%>
      <a id='reg-log' class='button' href='/reg-log.jsp'><i class='fa fa-sign-in' aria-hidden='true'></i> Logowanie/Rejestracja</a>
    </c:otherwise>

  </c:choose>

  <div style="clear: both;"></div>

</nav>

<script type="text/javascript">
    $("#${param.navID}").css("background-color","#4E986F");
</script>
