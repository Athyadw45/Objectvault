import os

def getConstants():
    constants = {}
    constants["OBJECTVAULT_HOST"] = os.environ.get('OBJECTVAULT_HOST', 'localhost')
    constants["OBJECTVAULT_PORT"] = os.environ.get('OBJECTVAULT_PORT', '8501')

    return constants