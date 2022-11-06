import React from 'react';
import { Component } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import '../resources/css/form.css';

export default class NewPublication extends Component{

    constructor(props) {
        super(props);
        this.state = {
            title: "",
            body: ""
        }
    }

    setValues = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    }

    cleanValues = () => {
        this.setState(
            {
                title: "",
                body: ""
            }
        );
    }

    post = (event) => {
        event.preventDefault();

        const url = "http://localhost:8080/publications/insert";
        const publication = {
            title: this.state.title,
            body: this.state.body
        }
        const header = {
            method: "POST",
            body: JSON.stringify(publication),
            headers: {
                "Content-Type": "application/json",
                "credential": localStorage.getItem("uuid")
            }
        }

        fetch(url, header)
            .then(response => {
                if (!response.ok) throw Error(response.status);
                return response.json();
            }
            )
            .then(json => {
                console.log(json);
                window.location.href="/publications";
            })
            .catch(error => {
                console.error(error);
                localStorage.clear();
                alert("Error al realizar la publicacion. Inicia sesion de nuevo");
                window.location.href="/";
            });
        this.cleanValues();
    }

    render() {
        return(
            <>
                <Form className="formPost" onSubmit={this.post}>
                    <Form.Group className="mb-3 formInputs" controlId="title">
                        <Form.Label>Titulo</Form.Label>
                        <Form.Control 
                            type="text" 
                            controlid="title"   
                            name="title"
                            placeholder="Titulo de la publicacion" 
                            value={this.state.title}
                            onChange={this.setValues}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3 formInputs" controlId="body">
                        <Form.Label>Cuerpo</Form.Label>
                        <Form.Control 
                            as="textarea" 
                            controlid="body"
                            name="body"
                            rows={3} 
                            value={this.state.body}
                            onChange={this.setValues}
                            placeholder="Escribe la publicacion..."
                        />
                    </Form.Group>

                    <Button 
                        variant="primary"  
                        type="submit"
                        className="formButton"
                        size="lg">
                        Publicar
                    </Button>
                    <Button 
                        variant="secondary"     
                        type="reset" 
                        className="formButton"
                        size="lg"
                        onClick={this.cleanValues}>
                        Limpiar
                    </Button>
                </Form>


            </>
        );
    }
}
