<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../components/page_head.jsp">
  <jsp:param name="title" value="JAVA - admin"/>
</jsp:include>

<nav class="subNav">

  <a id="Z" class="button" href="/admin-foto">
    <i class="fa fa-file-image-o"></i>	Zdjęcia
  </a>

  <a id='K' class='button' href="/admin-comments">
    <i class='fa fa-comments-o'></i>	Komentarze
  </a>

  <c:if test = "${user.isAdmin()}">
    <a id='A' class='button' href="/admin-users">
      <i class='fa fa-folder'></i>	Użytkownicy
    </a>
  </c:if>

  <a class='powrot button' href='/'>
    <i class='fa fa-arrow-circle-left fa-lg' aria-hidden='true'></i>
    <i class='fa fa-arrow-circle-o-left fa-lg' aria-hidden='true'></i>
    Powrót do GALERII
  </a>

  <div style="clear: both;"></div>

</nav>

<section>

  <div style="clear:both;"></div>

</section>

<jsp:include page="../components/page_footer.jsp"/>