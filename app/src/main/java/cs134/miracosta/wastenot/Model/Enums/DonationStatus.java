package cs134.miracosta.wastenot.Model.Enums;

/**
 * The <code>DonationStatus</code> enum stores the status of a donation.
 * UNCLAIMED: Donation has been made by Donor, but no Claimer has requested it.
 * CLAIMED: A Claimer has requested the Donation, but no Driver has yet claimed the Delivery
 * DELIVERY_CLAIMED: A Driver has claimed the Donation. It will keep this status until Delivery where
 *                   it will be deleted.
 *
 * @author Will Craycroft
 */
public enum DonationStatus
{
    UNCLAIMED,
    DONATION_CLAIMED,
    DELIVERY_CLAIMED
}
