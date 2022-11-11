import React from 'react';
import { Component } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import CloseButton from 'react-bootstrap/CloseButton';
import '../resources/css/form.css';

// ES6 Modules or TypeScript
import Swal from 'sweetalert2';

export default class UpdatePublication extends Component{

    constructor(props) {
        super(props);
        this.state = {
            id: this.props.id,
            title: this.props.title,
            body: this.props.body
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

        const url = "http://localhost:8080/publications/update";
        const publication = {
            id: this.state.id,
            title: this.state.title,
            body: this.state.body
        }
        const header = {
            method: "PUT",
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
                Swal.fire({
                    title: 'Error al actualizar publicacion. Inicia sesion de nuevo',
                    icon: 'info',
                    confirmButtonText: 'Ok'
                }).then((result) => {
                    if (result['isConfirmed']){
                        window.location.href="/";
                    }
                })
                
            });
        this.cleanValues();
    }

    render() {
        return(
            <div className="formUpdate">
                <Form className="formPost" onSubmit={this.post}>
                    <CloseButton onClick={this.props.closeModal}/>
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

                    <span className='publish-buttons'>
                        <Button
                            variant="secondary"
                            type="reset"
                            className="formButton"
                            size="lg"
                            onClick={this.cleanValues}>
                            Limpiar
                        </Button>
                        <Button
                            variant="primary"
                            type="submit"
                            className="formButton"
                            size="lg">
                            Actualizar
                        </Button>
                    </span>
                </Form>
            </div>
        );
    }
}