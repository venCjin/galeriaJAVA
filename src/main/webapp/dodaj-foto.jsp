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

  <c:if test="${requestScope.error != null}">
    <p class="error" style="width: 100%; text-align:center;"><i style="color: #EB3232;" class="fa fa-exclamation-circle" aria-hidden="true"></i> <c:out value="${requestScope.error}"/> </p>
    <br>
  </c:if>

  <%--  <c:if test="${requestScope.success != null}">--%>
  <%--    <p class="success" style="width: 100%; text-align:center;">--%>
  <%--      <c:out value="${requestScope.success}"/>--%>
  <%--    </p>--%>

  <%--    <c:if test="${requestScope.id_albumu != null && requestScope.id_foto != null}">--%>
  <%--      <div class='foto'>--%>
  <%--        <img src='img/${requestScope.id_albumu}/${requestScope.id_foto}'>--%>
  <%--      </div>--%>
  <%--    </c:if>--%>
  <%--    <br>--%>
  <%--  </c:if>--%>
  <c:if test="${param.success != null}">
    <p class="success" style="width: 100%; text-align:center;">
      <c:out value="${param.success}"/>
    </p>

    <c:if test="${param.id_albumu != null && param.id_foto != null}">
      <div class='foto'>
        <img id="sendedFoto" class="miniatura" style="float: none;" src='img/${param.id_albumu}/${param.id_foto}'>
      </div>
    </c:if>
    <script>
      // document.onload= (e) {
        var img = $("#sendedFoto");
        img.attr('dt-retry', 60);
        var src = img[0].src;
        console.log("src: ",src);
        // imageArray[i] = new Image();
        // imageArray[i].src = document.images[i].src;
        //try to reload image in case of error
        //retry every 1sec for 60 times
        var imgErrorFunction = async function () {
          try {
            console.log("imgErrorFunction: ",this);
            await new Promise(resolve => setTimeout(resolve, 1000));
            // let src = this.src;
            console.log("src: ",src+"?timestamp=" + new Date().getTime());
            this.onerror = null;
            this.onerror = imgErrorFunction;
            this.removeAttr("src").attr("src", src+"?timestamp=" + new Date().getTime());
            console.log("imgErrorFunction2: ",this);
            console.log("------------");
            // window.location.reload();

            // var img = this;
            // var isRet = true;
            // var r = 60;
            // if (img.hasAttribute('dt-retry')) {
            //   r = parseInt(img.getAttribute('dt-retry'));
            //   r = r - 1;
            //   img.setAttribute('dt-retry', r);
            //   if (r <= 0) {
            //     isRet = false;
            //     //Force a hard reload to clear the cache if supported by the browser
            //     console.log("isRet: ",isRet);
            //     window.location.reload(true);
            //   }
            // }
            //
            // if (isRet) {
            //   var temp = new Image();
            //   temp.setAttribute('dt-retry', r);
            //   temp.onerror = imgErrorFunction;
            //   temp.src = img.src;
            // }
          } catch (e) {}
        }

        console.log("src: ",src+"?timestamp=" + new Date().getTime());
        img.onerror = null;
        img.onerror = imgErrorFunction;
        img.removeAttr("src").attr("src", src+"?timestamp=" + new Date().getTime());

      // }
    </script>
    <br>
  </c:if>

  <br>

  Wybierz album do którego chcesz dodać zdjęcie:

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