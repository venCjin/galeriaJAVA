<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="components/page_head.jsp">
  <jsp:param name="title" value="JAVA - dodaj-album"/>
</jsp:include>

<jsp:include page='components/nav.jsp'>
  <jsp:param name="navID" value="add-z"/>
</jsp:include>

<section>

  <h2>
    <b>Dodawanie zdjęć</b>
  </h2>

  Wybierz album do którego chcesz dodać zdjęcie:

  <br>
  <c:if test="${requestScope.error != null}">
    <p class="error" style="width: 100%; text-align:center;"><i style="color: #EB3232;" class="fa fa-exclamation-circle" aria-hidden="true"></i> <c:out value="${requestScope.error}"/> </p>
    <br>
  </c:if>
  <c:if test="${requestScope.success != null}">
    <p class="success" style="width: 100%; text-align:center;">
      <c:out value="${requestScope.success}"/>
    </p>
    <c:if test="${requestScope.id_albumu != null && requestScope.id_foto != null}">
      <div class='foto'>
        <img src='img/${requestScope.id_albumu}/${requestScope.id_foto}'>
      </div>
    </c:if>
    <br>
  </c:if>

  <form method='POST' enctype="multipart/form-data" action='/dodaj-foto'>
    <div class="input-group folder">
      <select class="form-control folder" name='id_albumu'><!-- onchange="myFun(this.value)"> -->
		    <option selected='true' disabled='disabled'>---Wybierz album---</option>
        <c:forEach items="${requestScope.albums}" var="album">
          <option class='form-control folder' value='${album.getIdAlbum()}'>
            <c:out value="${album.getTitle()}"/>
          </option>
        </c:forEach>
      </select>
    </div>

  <br>

  Dodaj zdjęcie i opis(opcjonalny, nie dłuższy niż 100 znaków):

    <div class="input-group folder">
      <span class="input-group-addon"><i class="fa fa-font fa-fw" aria-hidden="true"></i></span>
      <input class="form-control folder" type="text" name="opis" maxlength="255" placeholder="Opis(opcjonalny)">
    </div>

    <div class="input-group margin-bottom-sm">
      <span class="input-group-addon"><i class="fa fa-file-image-o" aria-hidden="true"></i></span>
      <div class="form-control folder light">
        <input id="input_file" type="file" name="zdjecie" required>
      </div>
    </div>

    <input type="submit" value="Wyślij!">

  </form>

  <br>

  <div style="clear:both;"></div>

</section>

<jsp:include page="components/page_footer.jsp"/>