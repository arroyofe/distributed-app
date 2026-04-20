import pymysql

def classify_error(e):
    if isinstance(e, IOError):
        return "Archivo"
    if isinstance(e, pymysql.err.MySQLError):
        return "Base de datos"
    return "General"
