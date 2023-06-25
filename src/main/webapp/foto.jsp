<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="components/page_head.jsp">
  <jsp:param name="title" value="JAVA - galeria"/>
</jsp:include>

<jsp:include page='components/nav.jsp'>
  <jsp:param name="navID" value="galeria"/>
</jsp:include>

<section>

<%--  <a class='powrot' href='index.php?strona=galeria&page=$pg_g'>
<i class='fa fa-arrow-circle-left fa-lg' aria-hidden='true'></i>
<i class='fa fa-arrow-circle-o-left fa-lg' aria-hidden='true'></i>
Powrót</a>--%>
  <br>
  <c:if test="${requestScope.msg != null}">
    <c:out value="${requestScope.msg}"/>
  </c:if>

<%-- INFO O ZDJECIU --%>
  <div class='foto'>
    Dodany przez: <c:out value="${requestScope.foto.getAuthor()}"/><br>
    Data dodania zdjęcia: <c:out value="${requestScope.foto.getDate()}"/>
    <c:if test="${requestScope.foto.hasDesc()}">
      <br>
      Opis zdjęcia: <c:out value="${requestScope.foto.getDescFull()}"/>
    </c:if>
  </div>
  <br>
<%-- -------------- --%>

<%-- ZDJĘCIE --%>
  <div class='foto'>
    <img src='img/${requestScope.foto.getIdAlbum()}/${requestScope.foto.getIdPhoto()}'>
  </div>
<%-- ------- --%>
  <br>

<%-- OCENY --%>
  <div class='foto'>Ocena: <c:out value="${requestScope.rating}"/>
    (Liczba oceniających: <c:out value="${requestScope.rating_count}"/>)
    <c:if test="${requestScope.can_rate}">
      <br>
      <span class='rating' onmouseleave="doRating($('#ocena').val())">
        <span title="10" id="s10" onclick="doRating(10);" onmouseenter="hoverRating(10)" class="star"></span>
        <span title="9"  id="s9"  onclick="doRating(9);"  onmouseenter="hoverRating(9)"  class="star"></span>
        <span title="8"  id="s8"  onclick="doRating(8);"  onmouseenter="hoverRating(8)"  class="star"></span>
        <span title="7"  id="s7"  onclick="doRating(7);"  onmouseenter="hoverRating(7)"  class="star"></span>
        <span title="6"  id="s6"  onclick="doRating(6);"  onmouseenter="hoverRating(6)"  class="star"></span>
        <span title="5"  id="s5"  onclick="doRating(5);"  onmouseenter="hoverRating(5)"  class="star"></span>
        <span title="4"  id="s4"  onclick="doRating(4);"  onmouseenter="hoverRating(4)"  class="star"></span>
        <span title="3"  id="s3"  onclick="doRating(3);"  onmouseenter="hoverRating(3)"  class="star"></span>
        <span title="2"  id="s2"  onclick="doRating(2);"  onmouseenter="hoverRating(2)"  class="star"></span>
        <span title="1"  id="s1"  onclick="doRating(1);"  onmouseenter="hoverRating(1)"  class="star"></span>
      </span>
<%--      <form method='POST' action='foto'>--%>
      <form method='POST' action='foto?id_zdjecia=${requestScope.foto.getIdPhoto()}'>
        <input type='hidden' name='ocena' id='ocena' value="0"/>
        <input type='submit' value='Oceń!'/>
      </form>
    </c:if>
  </div>
  <br>
<%-- ----- --%>

<%-- WYŚWIETLANIE KOMENTARZY --%>
<%--  <div class='foto'>--%>
<%--    <h3>Komentarze</h3>--%>
<%--    <c:forEach items="${requestScope.comments}" var="comment">--%>
<%--      <div class='kom'>--%>
<%--        $login • $data--%>
<%--        <br>--%>
<%--        <div class='komt'>$kom</div>--%>
<%--      </div>--%>
<%--      <br>--%>
<%--    </c:forEach>--%>
<%--    <c:if test="${requestScope.comments.isEmpty()}}">--%>
<%--      <div class='kom'>Nie dodano jeszcze żadnych komentarzy.</div>--%>
<%--      <br>--%>
<%--    </c:if>--%>
<%-- ----------------------- --%>

<%-- DODAWANIE KOMENTARZA --%>
<%--  <c:if test="${sessionScope.user != null}">--%>
<%--    <form action='dodaj-komentarz.php' method='post' enctype='multipart/form-data' accept='text/html'>--%>
<%--      <label>--%>
<%--        Dodaj komentarz:--%>
<%--      </label>--%>
<%--      <textarea cols='60' rows='5' name='komentarz' placeholder='Napisz komentarz'></textarea>--%>
<%--      <br>--%>
<%--      <input type='hidden' value='$id_zdjecia' name='id_zdjecia'></input>--%>
<%--      <input type='submit' value='Skomentuj'></input>--%>
<%--    </form>--%>
<%--  </c:if>--%>
<%--  <c:if test="${sessionScope.user == null}">--%>
<%--    Musisz być <a href='regiester'>zalogowany</a>, by dodać komentarz.--%>
<%--    <br>--%>
<%--  </c:if>--%>
<%--  </div>--%>
<%--  <br>--%>
<%-- -------------------- --%>

  <div style="clear:both;"></div>


<%--  <br>--%>
<%--  <a class='powrot' href='index.php?strona=galeria&page=$pg_g'>
<i class='fa fa-arrow-circle-left fa-lg' aria-hidden='true'></i>
<i class='fa fa-arrow-circle-o-left fa-lg' aria-hidden='true'></i>
Powrót</a>--%>

  <script type="text/javascript">
    function hoverRating(stars)
    {
      console.log("hover: "+stars);
      for (var id = 1; id <= 10; id++)
      {
        selector = '#s' + id;
        $(selector).removeClass('star-1');
      }

      for (var id = 1; id <= stars; id++)
      {
        selector = '#s' + id;
        $(selector).addClass('star-1');
      }
    }
    function doRating(stars)
    {
      console.log("do: "+stars);

      $('#ocena').val(stars);

      hoverRating(stars);
    }

    <c:if test="${requestScope.alert != null}">
      window.alert("${requestScope.alert}");
    </c:if>
  </script>

  <div style="clear:both;"></div>

</section>

<jsp:include page="components/page_footer.jsp"/>