<#-- @ftlvariable name="todos" type="kotlin.collections.List<yarvol.models.Todo>" -->
<#import "_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>Edit todo</h3>
        <form action="/todos/${todo.id}" method="post">
            <p>
                <input type="text" name="body" value="${todo.body}">
            </p>
            <p>
                <input type="submit" name="_action" value="update">
            </p>
        </form>
    </div>
    <div>
        <form action="/todos/${todo.id}" method="post">
            <p>
                <input type="submit" name="_action" value="delete">
            </p>
        </form>
    </div>
</@layout.header>