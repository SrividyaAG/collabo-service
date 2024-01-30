package com.emeritus.collabo.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.emeritus.collabo.constant.Constants;

/**
 * The Class MessagingConfig.
 */
@Configuration
public class MessagingConfig {

  /** The create course. */
  @Value("${canvaslms.course.create.event:course_created}")
  private String createCourse;

  /** The create enrollment. */
  @Value("${canvaslms.enrollment.create.event:enrollment_created}")
  private String createEnrollment;

  /**
   * Exchange.
   *
   * @return the topic exchange
   */
  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(Constants.RABBIT_EXCHANGE);
  }

  /**
   * Creates the course.
   *
   * @return the queue
   */
  @Bean
  public Queue createCourse() {
    return new Queue(createCourse);
  }

  /**
   * Creates the enrollment.
   *
   * @return the queue
   */
  @Bean
  public Queue createEnrollment() {
    return new Queue(createEnrollment);
  }

  /**
   * Binding.
   *
   * @return the binding
   */
  @Bean
  public Binding createCourseBinding() {
    return BindingBuilder.bind(createCourse()).to(exchange())
        .with(createCourse + Constants.ROUTING_KEY);
  }

  /**
   * Json binding.
   *
   * @return the binding
   */
  @Bean
  public Binding createEnrollmentBinding() {
    return BindingBuilder.bind(createEnrollment()).to(exchange())
        .with(createEnrollment + Constants.ROUTING_KEY);
  }

  /**
   * Converter.
   *
   * @return the message converter
   */
  @Bean
  public MessageConverter converter() {
    return new Jackson2JsonMessageConverter();
  }

  /**
   * Template.
   *
   * @param connectionFactory the connection factory
   * @return the amqp template
   */
  @Bean
  public AmqpTemplate template(ConnectionFactory connectionFactory) {
    final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(converter());
    return rabbitTemplate;
  }
}
