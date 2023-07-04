<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../components/page_head.jsp">
  <jsp:param name="title" value="JAVA - admin - użytkownicy"/>
</jsp:include>

<nav class="subNav">

  <a id="Z" class="button green" href="/admin-foto">
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

  <article id="tZdjecia">
    <c:if test="${requestScope.error != null}">
      <p class="error" style="margin: 0 0 5px 0;"><i style="color: #EB3232;" class="fa fa-exclamation-circle" aria-hidden="true"></i> ${requestScope.error}</p>
      <br>
    </c:if>

    <c:if test="${requestScope.success != null}">
      <p class="success" style="width: 100%; text-align:center;"> <c:out value="${requestScope.success}"/></p>
      <br>
    </c:if>

    <form method="get" action="/admin-foto">
      <div class="input-group margin-bottom-sm">
        <select class="form-control" name="z_type" onchange="this.form.submit()">
          <option selected="true" disabled="disabled">---Wybierz typ zdjęcia---</option>
          <option id="z1" value="all">Wszystkie</option>
          <option id="z2" value="to_acpt">Do akceptacji</option>
        </select>
      </div>
    </form>

    <c:if test="${requestScope.z_type != null}">
      <form method="post" action="/admin-foto">

        <div class="input-group margin-bottom-sm">
          <select class="form-control" name="id_zdjecia" onchange="async_z(this)">
            <option selected="true" disabled="disabled">---Wybierz zdjęcie---</option>
            <c:forEach items="${requestScope.items}" var="foto">
              <option value='${foto.getIdPhoto()}'
                      data-album_id='${foto.getIdAlbum()}'
<%--                      data-autor='${foto.getIdAlbum()}'--%>
                      data-data='${foto.getDate()}'
                      data-opis='${foto.getDescFull()}'
              >${foto.getDescShort()}</option>
            </c:forEach>
          </select>
        </div>

        <input type='hidden' name='id_albumu' id='id_albumu' value=""/>

        <input type='hidden' name='z_type' value="${requestScope.z_type}"/>

        <c:if test="${requestScope.z_type == 'to_acpt'}">
          <input type='submit' name='akcpt_z' value='Zaakceptuj'>
        </c:if>
        <input type='submit' name='usn_z' value='Usuń' onclick='return confirm("Czy na pewno chcesz usunąć to zdjęcie?")'>

      </form>

    <script>
      <c:if test="${requestScope.z_type == 'all'}">
      $("#z1").prop("selected", true);
      </c:if>
      <c:if test="${requestScope.z_type == 'to_acpt'}">
        $("#z2").prop("selected", true);
      </c:if>
      // var opt;
      function async_z(select) {
          // opt = select;
          // console.log("e: ",select);
          let id_z = select.value;
          // console.log("id_z: "+id_z);
          let id_a = $(select).find(':selected').data('album_id');
          // console.log("id_a: "+id_a);
          let date = $(select).find(':selected').data('data');
          let desc = $(select).find(':selected').data('opis');
          $("#id_albumu").val(id_a);
          $('#zdjecie').html(
              "<div class='foto margin-top-sm'> " + //"Dodany przez: "+userName+"<br>" +
              "Data dodania zdjęcia: " + date +
              "<br>Opis zdjęcia: " + desc +
              "</div>"+
              "<div id='galeria-div' style='width:192px; height:192px;'><a class='dymek'><img class='miniatura' src='../img/"+id_a+"/"+id_z+"' width='180px' height='180px'></a></div"
          )
      }

      // function ajax_z(id)
      // {
      //   $.ajax
      //   ({
      //     url: '/ajax_zdjecie',
      //     data: {id_zdjecia: id},
      //     success: function(i)
      //     {
      //         $('#zdjecie').html(i)
      //     }
      //   })
      // }
    </script>

    <div id='zdjecie'></div>
    </c:if>

  </article>

  <div style="clear:both;"></div>

</section>

<jsp:include page="../components/page_footer.jsp"/>