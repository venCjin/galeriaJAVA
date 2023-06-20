<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="components/page_head.jsp">
  <jsp:param name="title" value="JAVA - galeria"/>
</jsp:include>

<jsp:include page='components/nav.jsp'>
  <jsp:param name="navID" value="galeria"/>
</jsp:include>

<section>

  <div id="galeria-div">

    <c:if test="${requestScope.msg != null}">
      <c:out value="${requestScope.msg}"/>
    </c:if>

    <c:forEach items="${requestScope.gallery_items}" var="item">
      <div class='dymek'>
        <a href='/album?id_albumu=${item.getIdAlbum()}'>
          <span>
            <div>
              <p>
                Tytuł galerii: <c:out value="${item.getTitle()}"/>
                <br>
                Autor: <c:out value="${item.getAuthor()}"/>
                <br>
                Data utworzenia: <c:out value="${item.getDate()}"/>
              </p>
            </div>
          </span>
          <img class='miniatura' src='img/${item.getIdAlbum()}/${item.getIdPhoto()}' width='180px' height='180px'>
        </a>
      </div>
    </c:forEach>

    <div style="clear:both;"></div>
  </div>

  <div class="page">

    <label class="sort-label">Strony: </label>
    <c:forEach items="${requestScope.page_items}" var="i">
        <a id='pg${i}' class='button' href='/?page=${i}&sort=${requestScope.sortParam}'>${i}</a>
    </c:forEach>

    <div style="clear: both;"></div>

    <label class="sort-label">Sortowanie: </label>
    <a id="sort-t" class="button" href='/?sort=sort-t'>tytul albumu</a>
    <a id="sort-n" class="button" href='/?sort=sort-n'>nick autora</a>
    <a id="sort-d" class="button" href='/?sort=sort-d'>data dodania</a>

    <div style="clear: both;"></div>
  </div>

  <script type="text/javascript">
    $("#${requestScope.sortParam}").css("background-color","#4E986F");
    $("#pg${requestScope.page}").css("background-color","#4E986F");
  </script>

  <div style="clear:both;"></div>

</section>

<jsp:include page="components/page_footer.jsp"/>