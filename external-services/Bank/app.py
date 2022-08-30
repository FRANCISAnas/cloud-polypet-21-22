from flask import Flask, request
import os


app = Flask(__name__)


@app.route('/', methods=["POST"])
def payment():
    return "SUCCESS"

@app.route('/', methods=['GET'])
def index():
    return "WELCOME TO BANK HOME PAGE ! :-)"

@app.route('/status')
def status():
    return 'READY'



if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=int(os.getenv('PORT', 5002)))
