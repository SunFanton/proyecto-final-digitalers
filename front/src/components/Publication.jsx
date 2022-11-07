import React from 'react';
import { Component } from 'react';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrash, faFolder, faPencil } from "@fortawesome/free-solid-svg-icons";
import '../resources/css/publication.css';

export default class Publication extends Component{

    constructor(props) {
        super(props);
        this.state = {
            id: this.props.id,
            title: this.props.title,
            body: this.props.body
        }
    }


    render() {
        return(
            <>
                <Card className="card" key={this.state.id}>
                    <Card.Body className="cardBody">
                        <Card.Title className="cardTitle">
                            <div className="titleDiv">
                                <FontAwesomeIcon icon={faFolder} className="primary"/>
                                <h2 className="title">{this.state.title}</h2>
                            </div>
                        </Card.Title>
                        <Card.Text className="cardtext">
                            {this.state.body}
                        </Card.Text>
                    </Card.Body>
                    <div className="buttonsCard">
                        <Button variant="danger">
                            <FontAwesomeIcon icon={faTrash} />
                        </Button>
                        <Button variant="secondary">
                            <FontAwesomeIcon icon={faPencil} />
                        </Button>
                    </div>
                </Card>
            </>
        );
    }
}