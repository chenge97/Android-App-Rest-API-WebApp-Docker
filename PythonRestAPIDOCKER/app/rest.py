import mysql.connector, random , string
from flask import Flask, render_template, flash, request, redirect, url_for, g, session
from wtforms import Form, TextField, TextAreaField, validators, StringField, SubmitField
from flask_restful import Resource, Api, reqparse
from werkzeug.utils import secure_filename
import os,json,cv2,werkzeug
from keras.models import load_model
import numpy as np
from keras.models import Sequential
from keras.layers import *
from keras.optimizers import *
import keras.backend as K
import Crypto, base64, logging, sys
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_v1_5


logging.basicConfig(level=logging.DEBUG)
ALLOWED_EXTENSIONS = set([ 'png', 'jpg', 'jpeg'])
UPLOAD_FOLDER = 'static/images/predict'

app = Flask(__name__)
app.config.from_object(__name__)
app.config['SECRET_KEY']='ncjcurbcruncj'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
api = Api(app)

parser = reqparse.RequestParser()
parser.add_argument('file',type=werkzeug.datastructures.FileStorage, location='files')
parser.add_argument('json' ,type=str, location='json')


def encrypt(string):
     private_key2 ="MIICXAIBAAKBgQCFjZH76hgnqI5d0GmZGY5M7yIaYBZG6j7TNJl24BPORV2zC9pODoLyAbf25b3eXtui1HZZe4oLJziv3mlE5+dvVcWSe/pcBXFi6ZS/t0/PI2pCMVv7I1R0oECMYlqYQklrLWwvS+eBoZZOe8Trq7kBDFCAc1+Ic+vcgdW4mawlvwIDAQABAoGALKk3FGMoSLrZQQ4dAhHFwHyHjwJq8LQM5lxEpbgxZ11TBDkGe+vmZj+k4W/amq5mDIG4QhxKAYjQMB/UXDKg9eKeuDx5XSwKORy40YJtSqY1lMwgtcKwGQ7UxkqrFWlbZpDfA4fs15NuWtFLNNheJ1iFX3VDtJtIme+MMxvVv4ECQQC/dK/UUNbyi10xqsr5WS5Eu7EZlrNsB/qUapYJBSCMjS8DSfCIlX8EsoDfYzn5Wl+Trk2QaYjlGzePkhYQRCivAkEAspOqyFmmRzCIjrpFiVHSH2RJldxW9Hw7Qwq/RJ5+/mGja88eZU6+0DeBjGzWDYoiTetqUSVCziiQesQwHA/38QJAQJo9ImVMwnboMXQyHUVMaYDz13CUhmWC1kXI7q4+N28EaBWxBkV7oLgi6D3xOASYr5pnLc2OldBDRTzEGSUGnQJAbEEqadQ3AbcBQYzYNJueRpt0JF3zdLiO8GBmfMGceLdV6zge1Ak9kVnkte0Qghq4GwZYaCKvceyTUWj3RTvE8QJBAJxXjlRGUTEuwEGovZison+Th4RuROuECiV9rf7MMj6aolisWCbR5r+DQKT5M4Wq+Yu9CgPxIRuAK42jKRKahko=" 
     private_key = base64.b64decode(private_key2)
     private_key = RSA.importKey(private_key)
     public_key = private_key.publickey()
     encryptor = PKCS1_v1_5.new(public_key)
     str_crypted = encryptor.encrypt(bytes(string,'utf-8'))
     str_crypted = base64.b64encode(str_crypted)
     str_crypted = str(str_crypted,'utf-8')
     print(str_crypted, file=sys.stderr)
     return str_crypted


def decrypt(str_crypted):
     private_key2 ="MIICXAIBAAKBgQCFjZH76hgnqI5d0GmZGY5M7yIaYBZG6j7TNJl24BPORV2zC9pODoLyAbf25b3eXtui1HZZe4oLJziv3mlE5+dvVcWSe/pcBXFi6ZS/t0/PI2pCMVv7I1R0oECMYlqYQklrLWwvS+eBoZZOe8Trq7kBDFCAc1+Ic+vcgdW4mawlvwIDAQABAoGALKk3FGMoSLrZQQ4dAhHFwHyHjwJq8LQM5lxEpbgxZ11TBDkGe+vmZj+k4W/amq5mDIG4QhxKAYjQMB/UXDKg9eKeuDx5XSwKORy40YJtSqY1lMwgtcKwGQ7UxkqrFWlbZpDfA4fs15NuWtFLNNheJ1iFX3VDtJtIme+MMxvVv4ECQQC/dK/UUNbyi10xqsr5WS5Eu7EZlrNsB/qUapYJBSCMjS8DSfCIlX8EsoDfYzn5Wl+Trk2QaYjlGzePkhYQRCivAkEAspOqyFmmRzCIjrpFiVHSH2RJldxW9Hw7Qwq/RJ5+/mGja88eZU6+0DeBjGzWDYoiTetqUSVCziiQesQwHA/38QJAQJo9ImVMwnboMXQyHUVMaYDz13CUhmWC1kXI7q4+N28EaBWxBkV7oLgi6D3xOASYr5pnLc2OldBDRTzEGSUGnQJAbEEqadQ3AbcBQYzYNJueRpt0JF3zdLiO8GBmfMGceLdV6zge1Ak9kVnkte0Qghq4GwZYaCKvceyTUWj3RTvE8QJBAJxXjlRGUTEuwEGovZison+Th4RuROuECiV9rf7MMj6aolisWCbR5r+DQKT5M4Wq+Yu9CgPxIRuAK42jKRKahko="
     private_key = base64.b64decode(private_key2)
     private_key = RSA.importKey(private_key)
     cipher = PKCS1_v1_5.new(private_key)
     str_crypted = str_crypted.replace(' ','+')
     str_crypted = str_crypted.replace('@','/')
     str_decrypt = cipher.decrypt(base64.b64decode(str_crypted),'Error')
     str_decrypt = str_decrypt.decode('utf-8')
     print(str_decrypt, file=sys.stderr)
     return str_decrypt


def getMysqlConnection():
     con = mysql.connector.connect(user='root',host='db', password='root', database='hola')
     return con
@app.before_request
def before_request():
        g.user = None
        if 'user' in session:
            g.user = session['user']

@app.route('/', methods=['GET'])
def index():
        if g.user:
            return redirect(url_for('loged'))
        return render_template('index.html')

@app.route('/' , methods=['POST'])
def check_user_exist():
        email = request.form['email']
        password = request.form['password']
        db = getMysqlConnection()
        cursor = db.cursor()
        cursor.execute("SELECT email, password FROM users where email='"+email+"'")
        row = cursor.fetchone()
        if row is not None:
            if row[1] == password:
                session['user'] = email
                cursor.close()
                db.close()
                return redirect(url_for('loged'))
            else:
                return redirect(url_for('index'))
        else:
            return redirect(url_for('index'))

@app.route('/register/', methods=['GET'])
def register():
        if g.user:
            redirect(url_for('loged'))
        return render_template("register.html")

@app.route('/register/' , methods=['POST'])
def check_user_exist2():
        name = request.form['name']
        email = request.form['email']
        password = request.form['password']
        db = getMysqlConnection()
        cursor = db.cursor()
        cursor.execute("SELECT email FROM users where email='"+email+"'")
        row = cursor.fetchone()
        if row is None:
            cursor.execute("INSERT INTO users (name, email,password) VALUES (%s,%s,%s)",( name,email,password))
            db.commit()
            cursor.close()
            db.close()
            session['user'] = email
            return url_for('loged')
        else:
            return url_for('register')

@app.route('/loged/', methods=['GET','POST'])
def loged():

        jdata = {}
        db = getMysqlConnection()
        cursor = db.cursor()
        cursor.execute("SELECT id FROM users where email='"+g.user+"'")
        row = cursor.fetchone()
        if row is not None:
           cursor.execute("SELECT image_name FROM images where id_user="+str(row[0]))
           row2 = cursor.fetchall()
           for r in row2:
              print('Image name'+ r[0])
              jdata.update({r[0]:None})
        cursor.close()
        db.close()

        if request.method == 'POST':
           if request.form['action'] == "delete":
             name = request.form['delete_image']
             db = getMysqlConnection()
             cursor = db.cursor()
             cursor.execute("SELECT id FROM users WHERE email='"+g.user+"'")
             row = cursor.fetchone()
             if row is not None:
               cursor.execute("DELETE FROM images WHERE image_name='"+name+"' AND id_user="+str(row[0]))
               db.commit()
               db.close()
               return redirect(url_for('loged'))
             else:
               return "Error"
           else:
             K.clear_session()
             file = request.files['file']
             if file.filename == '':
               flash('No selected file')
               return redirect(request.url)
             if file and allowed_file(file.filename):
               filename = secure_filename(file.filename)
               file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
               file = file.filename
               path = os.path.join(app.config['UPLOAD_FOLDER'],filename)
               img = cv2.imread(path,cv2.IMREAD_GRAYSCALE)
               img = cv2.resize(img,(64,64))
               data = img.reshape(1,64,64,1)
               model = load_model('my_model.h5')
               predict = model.predict([data])
               if np.argmax(predict)==1:
                  result = 'Dog'
               else:
                  result = 'Cat'
               db = getMysqlConnection()
               cursor = db.cursor()
               cursor.execute("SELECT id FROM users WHERE email='"+g.user+"'")
               row = cursor.fetchone()
               if row is not None:
                  id = row[0]
                  cursor.execute('INSERT INTO images (image_name,date,predicted_image_type,id_user) VALUES (%s, NOW(), %s,%s)',(file,result,id))
                  db.commit()
             db.close()
             K.clear_session()
             return redirect(url_for('loged'))
        return render_template('loged.html',jdata=jdata)

def allowed_file(filename):
        return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

class Users(Resource):
    def get(self):
        db = getMysqlConnection()
        cursor = db.cursor()
        cursor.execute('SELECT * FROM users')
        users = cursor.fetchall()
        list_json = []
        for u in users:
            json_dict = {"id": u[0] ,"name": u[1], "email": u[2], "password":u[3]}
            list_json.append(json_dict)
        json_response = {"data":list_json}
        response = app.response_class(response = json.dumps(json_response),status=200,mimetype='application/json')
        return response


class Add_user(Resource):
    def get(self,name,email,password):
        db = getMysqlConnection()
        cursor = db.cursor()
        cursor.execute("SELECT email FROM users where email='"+email+"'")
        row = cursor.fetchone()
        if row is None:
            cursor.execute("INSERT INTO users (name, email,password) VALUES (%s,%s,%s)",( name,email,password))
            db.commit()
            cursor.close()
            db.close()
            session['user'] = email
            message = 'User added'
        else:
            message = 'Error adding the new user'
        json_message = {"message": message}
        response = app.response_class(response = json.dumps(json_message),status=200,mimetype='application/json')
        return response

class Login(Resource):
    def post(self):
        par = request.get_data().decode('utf-8')
        par = par.replace('\n','')
        print(par, file=sys.stderr)
        if par is None:
            json_message = {"message": "Not Loged par null"}
            response = app.response_class(response = json.dumps(json_message),status=200,mimetype='application/json')
            return response

        json_data = json.loads(par)
        email_crypted = base64.b64encode(bytes('email','utf-8'))
        password_crypted = base64.b64encode(bytes('password','utf-8'))
        email_crypted = str(email_crypted,'utf-8')
        email_crypted = email_crypted+"\n"
        print('Email',file=sys.stderr)
        print(email_crypted, file=sys.stderr)
        password_crypted = str(password_crypted,'utf-8')
        password_crypted = password_crypted+"\n"
        print('Password', file=sys.stderr)
        print(password_crypted, file=sys.stderr)
        email_val_crypted = json_data[email_crypted]
        password_val_crypted = json_data[password_crypted]
        print(email_val_crypted, file=sys.stderr)
        print(password_val_crypted, file=sys.stderr)
        email = decrypt(email_val_crypted)
        password = decrypt(password_val_crypted)
        db = getMysqlConnection()
        cursor = db.cursor()
        cursor.execute("SELECT email, password FROM users where email='"+email+"'")
        row = cursor.fetchone()
        if row is not None:
            if row[1] == password:
                session['user'] = email
                cursor.close()
                db.close()
                message = 'Loged'
            else:
                message = 'Not Loged'

        else:
            message = 'Not Loged'
        message = encrypt(message)
        json_message = {"message": message}
        response = app.response_class(response = json.dumps(json_message),status=200,mimetype='application/json')
        return response



class Predictions(Resource):
    def get(self,identifier):
        db = getMysqlConnection()
        cursor = db.cursor()
        cursor.execute('SELECT * FROM images where id_user='+identifier)
        images = cursor.fetchall()
        list_json = []
        if images:
             for i in images:
                  date = str(i[2])
                  json_dict = {"id": i[0] ,"name": i[1], "date": date, "predict":i[3], "id_user": i[4]}
                  list_json.append(json_dict)
             json_response = list_json
             json_response2 = {"data": json_response}
             response = app.response_class(response = json.dumps(json_response2),status=200,mimetype='application/json')
             return response
        else:
             json_dict_error = {"error": "No such User id"}
             response = app.response_class(response = json.dumps(json_dict_error),status=200,mimetype='application/json')
             return response


class Img_list(Resource):
    def get(self):
        args = request.args
        email = args['email']
        email = decrypt(email)
        db = getMysqlConnection()
        cursor = db.cursor()
        cursor.execute("SELECT id FROM users where email='"+email+"'")
        id_user = cursor.fetchone()
        list_json = []
        if id_user is not None:
             cursor.execute("SELECT image_name FROM images WHERE id_user="+str(id_user[0]))
             images = cursor.fetchall()
             if images is not None:
                  for i in images:
                      i_crypted = encrypt(i[0])
                      j_image = {"image": i_crypted}
                      list_json.append(j_image)
                  json_dict = {"images": list_json}
                  json_response2 = json_dict
                  response = app.response_class(response = json.dumps(json_response2),status=200,mimetype='application/json')
                  return response
             else:
                  json_dict_error = {"error": "No such User id"}
                  response = app.response_class(response = json.dumps(json_dict_error),status=200,mimetype='application/json')
                  return response
        else:
             json_dict_error = {"error": "No such User id"}
             response = app.response_class(response = json.dumps(json_dict_error),status=200,mimetype='application/json')
             return response


class UploadPhoto(Resource):
    decorators=[]

    def post(self,email):
        data = parser.parse_args()
        if data['file'] == "":
            return {
                    'data':'',
                    'message':'No file found',
                    'status':'error'
                    }
        file = data['file']

        if file:
            K.clear_session()
            if file.filename == '':
              return {
                'data':'',
                'message':'Something when wrong',
                'status':'error'
                }

            if file and allowed_file(file.filename):
              filename = secure_filename(file.filename)
              file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
              file = file.filename
              path = os.path.join(app.config['UPLOAD_FOLDER'],filename)
              img = cv2.imread(path,cv2.IMREAD_GRAYSCALE)
              img = cv2.resize(img,(64,64))
              data = img.reshape(1,64,64,1)
              model = load_model('my_model.h5')
              predict = model.predict([data])
              if np.argmax(predict)==1:
                 result = 'Dog'
              else:
                 result = 'Cat'
              email = decrypt(email)
              db = getMysqlConnection()
              cursor = db.cursor()
              cursor.execute("SELECT id FROM users WHERE email='"+email+"'")
              row = cursor.fetchone()
              if row is not None:
                 id = row[0]
                 cursor.execute('INSERT INTO images (image_name,date,predicted_image_type,id_user) VALUES (%s, NOW(), %s,%s)',(file,result,id))
                 db.commit()
            db.close()
            K.clear_session()


            return {
                 'data':'',
                 'message':'photo uploaded',
                 'status':'success'
                   }
        return {
                'data':'',
                'message':'Something when wrong',
                'status':'error'
                }


class DeletePhoto(Resource):
    def get(self):
        check = 0
        args = request.args
        image = args['image']
        email = args['email']
        if image is None or email is None:
             return {
                'data':'',
                'message':'Missing arguments',
                'status':'error'
                }
        photo_name = decrypt(image)
        email = decrypt(email)
        db = getMysqlConnection()
        cursor = db.cursor()
        cursor.execute("SELECT id FROM users WHERE email='"+email+"'")
        cursor_email = cursor.fetchone()
        if cursor_email is not None:
             cursor.execute("SELECT image_name FROM images WHERE id_user="+str(cursor_email[0]))
             images = cursor.fetchall()
             if images is not None:
                   for i in images:
                       if(i[0] == photo_name):
                            cursor.execute("DELETE FROM images WHERE image_name='"+photo_name+"'")
                            db.commit()
                            if(os.path.isfile(UPLOAD_FOLDER+photo_name)):
                                  os.remove(UPLOAD_FOLDER+photo_name)
                            cursor.close()
                            db.close()
                            return {
                               'data':'',
                               'message':'Photo deleted successfully',
                               'status':'success'
                               }
                   if(check==0):
                            cursor.close()
                            db.close()
                            return {
                                    'data':'',
                                    'message':'No photo founded',
                                    'status':'error'
                                    }

             else:
                   cursor.close()
                   db.close()
                   return {
                          'data':'',
                          'message':'User has no photos to delete',
                          'status':'error'
                          }
        else:
             cursor.close()
             db.close()
             return {
                    'data':'',
                    'message':'Email error',
                    'status':'error'
                    }








api.add_resource(Users, '/users')
api.add_resource(Add_user, '/users/<string:name>/<string:email>/<string:password>')
api.add_resource(Login, '/login')
api.add_resource(Predictions, '/prediction/<string:identifier>')
api.add_resource(UploadPhoto,'/<string:email>/upload')
api.add_resource(DeletePhoto,'/delete')
api.add_resource(Img_list, '/imglist')



if __name__ == '__main__':
    app.run(debug=True,host='0.0.0.0')
