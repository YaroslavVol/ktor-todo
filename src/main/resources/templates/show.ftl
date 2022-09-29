<#-- @ftlvariable name="todos" type="kotlin.collections.List<yarvol.models.Todo>" -->
<#import "_layout.ftl" as layout />
<@layout.header>
    <div>
            <h3>
                ${todo.body}
            </h3>
            <hr>
            <p>
                <a href="/todos/${todo.id}/edit">Edit todo</a>
            </p>
        </div>
</@layout.header>