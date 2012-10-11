#{if session.username && controllers.Secure.Security.invoke("checkFuncionalidade", _arg)}
    #{doBody /}
#{/if}