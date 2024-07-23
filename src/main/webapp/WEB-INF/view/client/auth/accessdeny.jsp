<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>403 - Forbidden</title>
    <!-- Bootstrap CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body, html {
            height: 100%;
        }
        .bg {
            background-image: url('https://via.placeholder.com/1500x1000');
            height: 100%;
            background-position: center;
            background-repeat: no-repeat;
            background-size: cover;
        }
    </style>
</head>
<body>
    <div class="bg d-flex justify-content-center align-items-center">
        <div class="text-center p-5 bg-light rounded" style="opacity: 0.9;">
            <h1 class="display-4 text-danger">403</h1>
            <p class="lead">Không đủ quyền hạn để truy cập trang này.</p>
            <hr class="my-4">
            <p>Vui lòng liên hệ với quản trị viên để biết thêm chi tiết.</p>
            <a class="btn btn-primary btn-lg" href="/" role="button">Quay lại trang chủ</a>
        </div>
    </div>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
