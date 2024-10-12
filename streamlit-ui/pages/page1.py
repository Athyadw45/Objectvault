from navigation import make_sidebar
import streamlit as st
import requests

make_sidebar()

jwt_token = st.session_state.get('jwt_token')
uploaded_file = st.file_uploader("Choose a file")
filesList = None

def list_files():
    endpoint="http://35.208.81.217:3001/v1/list-files"
    headers={"Authorization":f"Bearer {jwt_token}"}
    r = requests.get(url=endpoint,headers=headers)

    if r.status_code==200:
        respBody=r.json()
        filesList = respBody['objectList']
        return filesList
    else:
        session_expired()

def upload_file(uploaded_file):
    
    endpoint="http://35.208.81.217:3001/v1/upload-file"
    headers={"Authorization":f"Bearer {jwt_token}"}
    if uploaded_file is not None:
        content_type = uploaded_file.type
        files = {'file': (uploaded_file.name, uploaded_file.getvalue(), content_type)}
        r = requests.post(url=endpoint, headers=headers, files=files)
        if r.status_code != 200:
            st.warning("File uplaod failed with status code "+str(r.status_code) +"\n"+ str(r.content),icon="⚠️")
            uploaded_file = None
            return 
        
        filesList = list_files()
        st.info("File uploaded successfully!! " + str(r.content),icon="ℹ️")
        uploaded_file=None

if st.button("Upload File"):
    if uploaded_file is not None:
        upload_file(uploaded_file)



def session_expired():
    st.session_state.logged_in = False
    st.session_state.jwt_token=None
    st.switch_page("app.py")

filesList = list_files()
 
st.write(filesList)







        

