<%-- 
    Document   : liste_voyage_activite
    Created on : Dec 19, 2023, 3:26:35 PM
    Author     : Family
--%>

<%@page import="java.util.Vector"%>
<%@page import="modele.ResteActivite"%>
<%
    Vector<ResteActivite> resteActivite = (Vector<ResteActivite>) request.getAttribute("reste");
    
%>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta content="width=device-width, initial-scale=1.0" name="viewport">

  <title>Forms / Elements - NiceAdmin Bootstrap Template</title>
  <meta content="" name="description">
  <meta content="" name="keywords">

  <!-- Favicons -->
  <link href="assets/img/favicon.png" rel="icon">
  <link href="assets/img/apple-touch-icon.png" rel="apple-touch-icon">

  <!-- Google Fonts -->
  <link href="https://fonts.gstatic.com" rel="preconnect">
  <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Nunito:300,300i,400,400i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i" rel="stylesheet">

  <!-- Vendor CSS Files -->
  <link href="assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <link href="assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
  <link href="assets/vendor/boxicons/css/boxicons.min.css" rel="stylesheet">
  <link href="assets/vendor/quill/quill.snow.css" rel="stylesheet">
  <link href="assets/vendor/quill/quill.bubble.css" rel="stylesheet">
  <link href="assets/vendor/remixicon/remixicon.css" rel="stylesheet">
  <link href="assets/vendor/simple-datatables/style.css" rel="stylesheet">

  <!-- Template Main CSS File -->
  <link href="assets/css/style.css" rel="stylesheet">

  <!-- =======================================================
  * Template Name: NiceAdmin
  * Updated: May 30 2023 with Bootstrap v5.3.0
  * Template URL: https://bootstrapmade.com/nice-admin-bootstrap-admin-html-template/
  * Author: BootstrapMade.com
  * License: https://bootstrapmade.com/license/
  ======================================================== -->
</head>

<body>
    <%@include file="header.jsp" %>
    
  <main id="main" class="main">

      
    <div class="pagetitle">
      <h1>Liste</h1>
    </div><!-- End Page Title -->

    <section class="section">
               <div class="card">
            <div class="card-body">
              <h5 class="card-title">Liste des voyages par activite</h5>

              <!-- Default Table -->
              <table class="table">
                <thead>
                  <tr>
                      <th scope="col">id</th>
                    <th scope="col">Activite</th>
                    <th scope="col">Reste</th>

                  </tr>
                </thead>
                <tbody>
                  <% for(int i=0 ; i<resteActivite.size() ; i++){ %>
                  <tr>
                      <td><%= resteActivite.get(i).getId() %></td>
                                            <td><%= resteActivite.get(i).getActivite() %></td>
                        <td><%= resteActivite.get(i).getQte() %></td>


                  </tr>
                  <% } %>

                 
                </tbody>
              </table>
              <!-- End Default Table Example -->
            </div>
          </div>
    </section>

  </main><!-- End #main -->

  <%@include file="footer.jsp" %>

  <a href="#" class="back-to-top d-flex align-items-center justify-content-center"><i class="bi bi-arrow-up-short"></i></a>

  <!-- Vendor JS Files -->
  <script src="assets/vendor/apexcharts/apexcharts.min.js"></script>
  <script src="assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="assets/vendor/chart.js/chart.umd.js"></script>
  <script src="assets/vendor/echarts/echarts.min.js"></script>
  <script src="assets/vendor/quill/quill.min.js"></script>
  <script src="assets/vendor/simple-datatables/simple-datatables.js"></script>
  <script src="assets/vendor/tinymce/tinymce.min.js"></script>
  <script src="assets/vendor/php-email-form/validate.js"></script>

  <!-- Template Main JS File -->
  <script src="assets/js/main.js"></script>

</body>

</html>
