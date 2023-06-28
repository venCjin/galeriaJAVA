<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="components/page_head.jsp">
  <jsp:param name="title" value="JAVA - dodaj-album"/>
</jsp:include>

<jsp:include page='components/nav.jsp'>
  <jsp:param name="navID" value="add-a"/>
</jsp:include>

<section>

  <h2>
    <b>Tworzenie albumu</b>
  </h2>

  <br>

  Podaj nazwę albumu (nie dłuższą niż 100 znaków):

  <form method="POST" action="/dodaj-album">
    <div class="input-group folder">
      <span class="input-group-addon"><i class='fa fa-folder' aria-hidden='true'></i></span>
      <input class="form-control folder" maxlength="100" type="text" name="nazwa_albumu" placeholder="Nazwa albumu" required>
    </div>

    <input type="submit" value="Stwórz!">
  </form>

  <c:if test="${requestScope.error != null}">
    <p class="error" style="width: 100%; text-align:center;">
      <i style="color: #EB3232;" class="fa fa-exclamation-circle" aria-hidden="true"></i>
      <c:out value="${requestScope.error}"/>
    </p>
  </c:if>

  <c:if test="${requestScope.success != null}">
    <br/>
    <p class="success" style="width: 100%; text-align:center;">
      <c:out value="${requestScope.success}"/>
    </p>
    <%--<br/>--%>
  </c:if>

  <br/>

  <div style="clear:both;"></div>

</section>

<jsp:include page="components/page_footer.jsp"/>