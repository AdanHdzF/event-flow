
    const schema = {
  "asyncapi": "3.0.0",
  "info": {
    "title": "Eventflow arquitectura orientada a eventos",
    "version": "1.0.0",
    "description": "Este es un ejemplo de una arquitectura orientada a eventos utilizando AsyncAPI. En esta arquitectura, los servicios se comunican a través de eventos, lo que permite una mayor flexibilidad y escalabilidad.\n",
    "contact": {
      "name": "Equipo de Soporte de Eventflow",
      "email": "eventflow@support.com"
    },
    "license": {
      "name": "MIT"
    }
  },
  "defaultContentType": "application/json",
  "servers": {
    "rabbitmq-dev": {
      "host": "rabbitmq:5672",
      "description": "RabbitMQ entorno de desarrollo.",
      "protocol": "amqp",
      "security": [
        {
          "type": "userPassword",
          "description": "Credenciales para autenticación en RabbitMQ."
        }
      ],
      "tags": [
        {
          "name": "development"
        }
      ]
    },
    "kafka-dev": {
      "host": "kafka:9092",
      "description": "Kafka entorno de desarrollo.",
      "protocol": "kafka",
      "tags": [
        {
          "name": "development"
        }
      ]
    }
  },
  "channels": {
    "orders.created": {
      "address": "orders.created",
      "description": "Canal para eventos de creación de órdenes.",
      "messages": {
        "orderCreatedEvent": {
          "name": "OrderCreatedEvent",
          "title": "Evento de creación de orden",
          "description": "Este evento se publica cuando se crea una nueva orden.",
          "contentType": "application/json",
          "payload": {
            "type": "object",
            "properties": {
              "product": {
                "type": "string",
                "x-parser-schema-id": "<anonymous-schema-1>"
              },
              "price": {
                "type": "number",
                "x-parser-schema-id": "<anonymous-schema-2>"
              }
            },
            "x-parser-schema-id": "OrderCreatedPayload"
          },
          "examples": [
            {
              "name": "laptopOrder",
              "summary": "Ejemplo de orden de laptop",
              "payload": {
                "product": "laptop-123",
                "price": 999.99
              }
            }
          ],
          "x-parser-unique-object-id": "orderCreatedEvent"
        }
      },
      "bindings": {
        "amqp": {
          "is": "routingKey",
          "exchange": {
            "name": "eventflow",
            "type": "topic",
            "durable": true,
            "autoDelete": false
          }
        }
      },
      "x-parser-unique-object-id": "orders.created"
    },
    "orders.cancelled": {
      "address": "orders.cancelled",
      "description": "Canal para eventos de cancelación de órdenes.",
      "messages": {
        "orderCancelledEvent": {
          "name": "OrderCancelledEvent",
          "title": "Evento de cancelación de orden",
          "description": "Este evento se publica cuando se cancela una orden.",
          "contentType": "application/json",
          "payload": {
            "type": "object",
            "properties": {
              "id": {
                "type": "string",
                "x-parser-schema-id": "<anonymous-schema-3>"
              },
              "reason": {
                "type": "string",
                "x-parser-schema-id": "<anonymous-schema-4>"
              }
            },
            "x-parser-schema-id": "OrderCancelledPayload"
          },
          "examples": [
            {
              "name": "laptopOrderCancelled",
              "summary": "Ejemplo de orden de laptop cancelada",
              "payload": {
                "id": "uuid",
                "reason": "Cliente solicitó la cancelación"
              }
            }
          ],
          "x-parser-unique-object-id": "orderCancelledEvent"
        }
      },
      "bindings": {
        "amqp": {
          "is": "routingKey",
          "exchange": {
            "name": "eventflow",
            "type": "topic",
            "durable": true,
            "autoDelete": false
          }
        }
      },
      "x-parser-unique-object-id": "orders.cancelled"
    },
    "inventory.reserved": {
      "address": "inventory.reserved",
      "description": "Canal para eventos de reserva de inventario.",
      "messages": {
        "inventoryReservedEvent": {
          "name": "InventoryReservedEvent",
          "title": "Evento de reserva de inventario",
          "description": "Este evento se publica cuando se reserva inventario para una orden.",
          "contentType": "application/json",
          "payload": {
            "type": "object",
            "properties": {
              "product": {
                "type": "string",
                "description": "ID del producto en la orden.",
                "x-parser-schema-id": "<anonymous-schema-5>"
              },
              "quantity": {
                "type": "integer",
                "description": "Cantidad de inventario reservada.",
                "x-parser-schema-id": "<anonymous-schema-6>"
              }
            },
            "x-parser-schema-id": "InventoryReservedPayload"
          },
          "examples": [
            {
              "name": "laptopOrderReserved",
              "summary": "Ejemplo de orden de laptop con inventario reservado",
              "payload": {
                "product": "laptop-123",
                "quantity": 1
              }
            }
          ],
          "x-parser-unique-object-id": "inventoryReservedEvent"
        }
      },
      "bindings": {
        "amqp": {
          "is": "routingKey",
          "exchange": {
            "name": "eventflow",
            "type": "topic",
            "durable": true,
            "autoDelete": false
          }
        }
      },
      "x-parser-unique-object-id": "inventory.reserved"
    },
    "payment.completed": {
      "address": "payment.completed",
      "description": "Canal para eventos de pago completado.",
      "messages": {
        "paymentCompletedEvent": {
          "name": "PaymentCompletedEvent",
          "title": "Evento de pago completado",
          "description": "Este evento se publica cuando se completa un pago para una orden.",
          "contentType": "application/json",
          "payload": {
            "type": "object",
            "properties": {
              "orderId": {
                "type": "string",
                "description": "ID de la orden para la cual se completó el pago.",
                "x-parser-schema-id": "<anonymous-schema-7>"
              }
            },
            "x-parser-schema-id": "PaymentCompletedPayload"
          },
          "examples": [
            {
              "name": "laptopPaymentCompleted",
              "summary": "Ejemplo de pago completado para una orden de laptop",
              "payload": {
                "orderId": "1324"
              }
            }
          ],
          "x-parser-unique-object-id": "paymentCompletedEvent"
        }
      },
      "bindings": {
        "amqp": {
          "is": "routingKey",
          "exchange": {
            "name": "eventflow",
            "type": "topic",
            "durable": true,
            "autoDelete": false
          }
        }
      },
      "x-parser-unique-object-id": "payment.completed"
    }
  },
  "operations": {
    "publishOrderCreated": {
      "action": "send",
      "description": "Publica un evento de creación de orden.",
      "channel": "$ref:$.channels.orders.created",
      "tags": [
        {
          "name": "orders"
        }
      ],
      "x-parser-unique-object-id": "publishOrderCreated"
    },
    "publishOrderCancelled": {
      "action": "send",
      "description": "Publica un evento de cancelación de orden.",
      "channel": "$ref:$.channels.orders.cancelled",
      "tags": [
        {
          "name": "orders"
        }
      ],
      "x-parser-unique-object-id": "publishOrderCancelled"
    },
    "publishInventoryReserved": {
      "action": "send",
      "description": "Publica un evento de reserva de inventario.",
      "channel": "$ref:$.channels.inventory.reserved",
      "tags": [
        {
          "name": "inventory"
        }
      ],
      "x-parser-unique-object-id": "publishInventoryReserved"
    },
    "onOrderCreatedInventory": {
      "action": "receive",
      "description": "Recibe un evento de creación de orden.",
      "channel": "$ref:$.channels.orders.created",
      "tags": [
        {
          "name": "inventory"
        }
      ],
      "x-parser-unique-object-id": "onOrderCreatedInventory"
    },
    "onOrderCancelledInventory": {
      "action": "receive",
      "description": "Recibe un evento de cancelación de orden.",
      "channel": "$ref:$.channels.orders.cancelled",
      "tags": [
        {
          "name": "inventory"
        }
      ],
      "x-parser-unique-object-id": "onOrderCancelledInventory"
    },
    "publishPaymentCompleted": {
      "action": "send",
      "description": "Publica un evento de pago completado.",
      "channel": "$ref:$.channels.payment.completed",
      "tags": [
        {
          "name": "payment"
        }
      ],
      "x-parser-unique-object-id": "publishPaymentCompleted"
    },
    "onOrderCreatedPayment": {
      "action": "receive",
      "description": "Recibe un evento de creación de orden.",
      "channel": "$ref:$.channels.orders.created",
      "tags": [
        {
          "name": "payment"
        }
      ],
      "x-parser-unique-object-id": "onOrderCreatedPayment"
    }
  },
  "components": {
    "messages": {
      "orderCreatedEvent": "$ref:$.channels.orders.created.messages.orderCreatedEvent",
      "orderCancelledEvent": "$ref:$.channels.orders.cancelled.messages.orderCancelledEvent",
      "inventoryReservedEvent": "$ref:$.channels.inventory.reserved.messages.inventoryReservedEvent",
      "paymentCompletedEvent": "$ref:$.channels.payment.completed.messages.paymentCompletedEvent"
    },
    "schemas": {
      "OrderCreatedPayload": "$ref:$.channels.orders.created.messages.orderCreatedEvent.payload",
      "OrderCancelledPayload": "$ref:$.channels.orders.cancelled.messages.orderCancelledEvent.payload",
      "InventoryReservedPayload": "$ref:$.channels.inventory.reserved.messages.inventoryReservedEvent.payload",
      "PaymentCompletedPayload": "$ref:$.channels.payment.completed.messages.paymentCompletedEvent.payload",
      "securitySchemes": {
        "rabbitmqCredentials": "$ref:$.servers.rabbitmq-dev.security[0]",
        "x-parser-schema-id": "securitySchemes"
      }
    }
  },
  "x-parser-spec-parsed": true,
  "x-parser-api-version": 3,
  "x-parser-spec-stringified": true
};
    const config = {"show":{"sidebar":true},"sidebar":{"showOperations":"byDefault"}};
    const appRoot = document.getElementById('root');
    AsyncApiStandalone.render(
        { schema, config, }, appRoot
    );
  