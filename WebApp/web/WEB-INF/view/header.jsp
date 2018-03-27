<%@ page import="com.sceptre.projek.webapp.CookieManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header>
    <div class="container">
        <table width="100%">
            <tr>
                <td>
                    <h1 class="left"><span class="header-title-1">PR</span>-<span class="header-title-2">OJEK</span></h1>
                </td>

                <td>
                    <p class="hi-username">Hi, <span class="bold"><%out.println(CookieManager.getUsername(request));%></span> !</p>
                </td>
            </tr>
            <tr>
                <td>
                    <p class="subtitle left">wushh... wushh... ngeeeeeenggg...</p>
                </td>
                <td>
                    <a class="logout right" href="logout">Logout</a>
                </td>
            </tr>
        </table>

        <ul class="nav-bar">
            <li>
                <a <% if(currentPage.equals("order")) { %>
				class="active" <% } %>
                href="order">Order</a></li>
            <li>
                <a <% if(currentPage.equals("history")) { %>
                        class="active" <% } %>
                href="my_previous_order">History</a></li>
            <li>
                <a <% if(currentPage.equals("profile")) { %>
                        class="active" <% } %>
                href="profile">My Profile</a></li>
        </ul>

    </div>
</header>

