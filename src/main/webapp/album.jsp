<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="components/page_head.jsp">
  <jsp:param name="title" value="JAVA - galeria"/>
</jsp:include>

<jsp:include page='components/nav.jsp'>
  <jsp:param name="navID" value="galeria"/>
</jsp:include>

<section>

<%--  <a class='powrot' href='index.php?strona=galeria&page=$pg_g'><i class='fa fa-arrow-circle-left fa-lg' aria-hidden='true'></i><i class='fa fa-arrow-circle-o-left fa-lg' aria-hidden='true'></i>Powrót</a>--%>

  <div id="galeria-div">

    <c:if test="${requestScope.msg != null}">
      <c:out value="${requestScope.msg}"/>
    </c:if>

    <c:forEach items="${requestScope.items}" var="item">
      <div class='dymek'>
        <a href='/foto?id_zdjecia=${item.getIdPhoto()}'>
          <span>
            <div>
              <p>
                Dodany przez: <c:out value="${item.getAuthor()}"/>
                <br>
                Data dodania zdjęcia: <c:out value="${item.getDate()}"/>
                <c:if test="${item.hasDesc()}">
                  <br>
                  Opis zdjęcia: <c:out value="${item.getDescShort()}"/>
                </c:if>
              </p>
            </div>
          </span>
          <img class='miniatura' src='img/${requestScope.id_albumu}/${item.getIdPhoto()}' width='180px' height='180px'>
        </a>
      </div>
    </c:forEach>

    <div style="clear:both;"></div>
  </div>

  <div class="page">

    <label class="sort-label">Strony: </label>
    <c:forEach items="${requestScope.page_items}" var="i">
        <a id='pg${i}' class='button' href='/album?id_albumu=${requestScope.id_albumu}&page=${i}'>${i}</a>
    </c:forEach>

    <div style="clear: both;"></div>

  </div>

<%--  <br>--%>
<%--  <a class='powrot' href='index.php?strona=galeria&page=$pg_g'><i class='fa fa-arrow-circle-left fa-lg' aria-hidden='true'></i><i class='fa fa-arrow-circle-o-left fa-lg' aria-hidden='true'></i>Powrót</a>--%>

  <script type="text/javascript">
    $("#pg${requestScope.page}").css("background-color","#4E986F");
  </script>

  <div style="clear:both;"></div>

</section>

<jsp:include page="components/page_footer.jsp"/>