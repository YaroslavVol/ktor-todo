<#import "_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>Create todo<h3>
        <form action="/todos" method="post">
                    <p>
                        <input type="text" name="title">
                    </p>
                    <p>
                        <input type="text" name="body">
                    </p>
                    <p>
                        <input type="submit">
                    </p>
                </form>
    <div>
</@layout.header>