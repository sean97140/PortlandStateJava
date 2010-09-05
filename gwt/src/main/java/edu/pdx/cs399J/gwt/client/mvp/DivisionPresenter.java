package edu.pdx.cs399J.gwt.client.mvp;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.pdx.cs399J.gwt.client.DivisionServiceAsync;

/**
 * A presenter that manages the state (model) of a division UI and interacts with the division view
 */
public class DivisionPresenter
{
    /** The view for this presenter */
    private final Display view;

    /** The remote service for performing division */
    private final DivisionServiceAsync service;

    /** The divisor */
    private Integer divisor = null;

    /** The dividend*/
    private Integer dividend = null;

    @VisibleForTesting
    Integer getDividend()
    {
        return this. dividend;
    }

    @VisibleForTesting
    Integer getDivisor()
    {
        return divisor;
    }

    @VisibleForTesting
    void setDivisor( int divisor )
    {
        this.divisor = divisor;
    }

    @VisibleForTesting
    void setDividend( int dividend )
    {
        this.dividend = dividend;
    }


    /**
     * The view interface for the division UI
     */
    public interface Display {
        void setDivisorChangeHandler( ValueChangeHandler<String> handler );

        public void setDivisorValid( boolean valid );

        void setDividendChangeHandler( ValueChangeHandler<String> handler );

        public void setDividendValid( boolean valid );

        public void setDivisionEnabled( boolean enabled );

        void setDivisionClickHandler( ClickHandler handler);

        void setErrorMessage( String message );

        void setQuotient( String quotient );
    }

    public DivisionPresenter( Display view, DivisionServiceAsync service ) {

        this.view = view;
        this.service = service;
        
        this.bind();
    }

    public void bind() {
       this.view.setDivisionEnabled( false );
        
       this.view.setDividendChangeHandler( new ValueChangeHandler<String>() {
           public void onValueChange( ValueChangeEvent<String> change )
           {
               try {
                 DivisionPresenter.this.dividend = Integer.parseInt(change.getValue());

               } catch (NumberFormatException ex) {
                 DivisionPresenter.this.dividend = null;
               }

               updateView();
           }
       });

      this.view.setDivisorChangeHandler( new ValueChangeHandler<String>() {

          public void onValueChange( ValueChangeEvent<String> event )
          {
              try {
                DivisionPresenter.this.divisor = Integer.parseInt(event.getValue());

              } catch (NumberFormatException ex) {
                DivisionPresenter.this.divisor = null;
              }

              updateView();
          }
      });

      this.view.setDivisionClickHandler( new ClickHandler() {
          public void onClick( ClickEvent clickEvent )
          {
              if (divisor == null) {
                  throw new RuntimeException("Shouldn't be able to perform division with a null divisor");
              }

              if (dividend == null) {
                  throw new RuntimeException("Shouldn't be able to perform division with a null dividend");
              }

              service.divide( dividend, divisor, new AsyncCallback<Integer>() {
                  public void onFailure( Throwable failure )
                  {
                      view.setErrorMessage(failure.toString());
                  }

                  public void onSuccess( Integer quotient )
                  {
                      view.setQuotient(String.valueOf(quotient));
                  }
              });
          }
      });
    }

    /**
     * Updates the state of the view based on the values of the dividend and the divisor
     */
    private void updateView()
    {
        boolean dividendValid = (dividend != null);
        view.setDividendValid( dividendValid );

        boolean divisorValid = (divisor != null);
        view.setDivisorValid( divisorValid );

        view.setDivisionEnabled( dividendValid && divisorValid );
    }
}